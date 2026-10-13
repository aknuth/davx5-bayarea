/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mikepenz.aboutLibraries.android)
    id("davx5.common-buildconfig")
}

android {
    defaultConfig {
        applicationId = "com.bayareaaffiliates.sync"

        // Bay Area Sync has its own versioning (same major version as the Outlook plugin). Overrides the
        // DAVx⁵ version from build-logic (AppVersion), version code scheme is the same: MmmppIIII
        versionCode = 500000000
        versionName = "5.0.0"

        base.archivesName = "bayareasync-$versionCode-$versionName"

        /* Android prevents having two apps installed with the same provider authority name. In that case,
        Google Play just shows a generic "Can't install DAVx5" message. So we derive the authority names
        from the package ID, so that the build variants (and clones) have their own authority names and
        can be installed beside DAVx5. */
        val webdavAuthority = "${applicationId}.provider.webdav"
        val debugInfoAuthority = "${applicationId}.provider.debuginfo"
        manifestPlaceholders["webdavAuthority"] = webdavAuthority
        manifestPlaceholders["debugInfoAuthority"] = debugInfoAuthority
        /* Override the default string values from the core library (core/src/main/res/values/strings.xml)
        so that code using getString(R.string.webdav_authority) etc. gets the correct authority. */
        resValue("string", "webdav_authority", webdavAuthority)
        resValue("string", "authority_debug_provider", debugInfoAuthority)

        /* Bay Area Sync: own account types, so that the app can be installed beside DAVx⁵ (two apps
        must not declare the same account type) */
        resValue("string", "account_type", applicationId!!)
        resValue("string", "account_type_address_book", "${applicationId}.address_book")

        // Currently no instrumentation tests for app-ose, so no testInstrumentationRunner
    }

    buildFeatures {
        compose = true
        resValues = true
    }

    // Java namespace for our classes (not to be confused with Android package ID)
    namespace = "com.davx5.ose"

    flavorDimensions += "distribution"
    productFlavors {
        create("ose") {
            dimension = "distribution"
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    // The dependencies info block is only useful for Google Play [https://developer.android.com/build/dependencies#dependency-info-play]
    // and encrypted with Google's public key (encryption is non-deterministic by design → breaks reproducible builds).
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            localDevices {
                create("virtual") {
                    device = "Pixel 3"
                    // TBD: API level 35 and higher causes network tests to fail sometimes, see https://github.com/bitfireAT/davx5-ose/issues/1525
                    // Suspected reason: https://developer.android.com/about/versions/15/behavior-changes-all#background-network-access
                    apiLevel = 34
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

    signingConfigs {
        /* Bay Area Sync: upload key for Google Play (Play App Signing). The key and its passwords are not
        in the repository, but in a properties file (keys: storeFile, storePassword, keyAlias, keyPassword),
        by default ~/.android-keys/bayareasync-upload.properties. If it doesn't exist, release builds are unsigned. */
        val signingProperties = file(
            System.getenv("BAYAREA_SIGNING_PROPERTIES")
                ?: "${System.getProperty("user.home")}/.android-keys/bayareasync-upload.properties"
        )
        if (signingProperties.exists()) {
            val props = Properties().apply { signingProperties.inputStream().use { load(it) } }
            fun property(key: String): String =
                props.getProperty(key) ?: throw GradleException("$key missing in $signingProperties")
            create("upload") {
                // relative paths are relative to the properties file
                storeFile = signingProperties.parentFile.resolve(property("storeFile"))
                storePassword = property("storePassword")
                keyAlias = property("keyAlias")
                keyPassword = property("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules-release.pro")

            isShrinkResources = true

            // Must be after signingConfigs {} block. Uncomment following line to test release build with debug signing key:
            // signingConfig = signingConfigs.getByName("debug")
            signingConfig = signingConfigs.findByName("upload")
        }
    }
}

dependencies {
    // include core subproject (manages its own dependencies itself, however from same version catalog)
    implementation(project(":core"))

    // Kotlin / Android
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)
    coreLibraryDesugaring(libs.android.desugaring)

    // Hilt
    implementation(libs.hilt.android.base)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.hilt.android.compiler)

    // support libs
    implementation(libs.androidx.core)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.viewmodel.base)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.base)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.materialIconsExtended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.toolingPreview)

    // own libraries
    implementation(libs.bitfire.cert4android)

    // third-party libs
    implementation(libs.guava)
    implementation(libs.okhttp.base)
    implementation(libs.openid.appauth)
}
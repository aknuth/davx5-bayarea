/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package com.davx5.ose.ui.intro

import at.bitfire.davdroid.ui.intro.BatteryOptimizationsPage
import at.bitfire.davdroid.ui.intro.IntroPageFactory
import at.bitfire.davdroid.ui.intro.PermissionsIntroPage
import at.bitfire.davdroid.ui.intro.WelcomePage
import javax.inject.Inject

class OseIntroPageFactory @Inject constructor(
    batteryOptimizationsPage: BatteryOptimizationsPage,
    permissionsIntroPage: PermissionsIntroPage
): IntroPageFactory {

    override val introPages = arrayOf(
        WelcomePage(),
        permissionsIntroPage,
        batteryOptimizationsPage
    )

}
/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package com.davx5.ose.ui.setup

import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import at.bitfire.davdroid.ui.setup.EmailLogin
import at.bitfire.davdroid.ui.setup.LoginInfo
import at.bitfire.davdroid.ui.setup.LoginType
import at.bitfire.davdroid.ui.setup.LoginTypesProvider
import at.bitfire.davdroid.ui.setup.LoginTypesProvider.LoginAction
import at.bitfire.davdroid.ui.setup.StandardLoginTypePage
import at.bitfire.davdroid.ui.setup.UrlLogin
import javax.inject.Inject

/**
 * Bay Area Sync: users only enter email address and password. The server is derived from the
 * email domain (`dav.<domain>`, see DavResourceFinder), so the login type page is always skipped.
 */
class BayAreaLoginTypesProvider @Inject constructor() : LoginTypesProvider {

    override val defaultLoginType = EmailLogin

    override fun intentToInitialLoginType(intent: Intent): LoginAction =
        when (intent.data?.normalizeScheme()?.scheme) {
            "caldavs", "carddavs", "davx5", "http", "https" ->
                LoginAction(UrlLogin, true)
            else ->
                LoginAction(EmailLogin, true)
        }

    @Composable
    override fun LoginTypePage(
        snackbarHostState: SnackbarHostState,
        selectedLoginType: LoginType,
        onSelectLoginType: (LoginType) -> Unit,
        setInitialLoginInfo: (LoginInfo) -> Unit,
        onContinue: () -> Unit
    ) {
        // not reachable because the login type page is always skipped
        StandardLoginTypePage(
            selectedLoginType = selectedLoginType,
            onSelectLoginType = onSelectLoginType,
            setInitialLoginInfo = setInitialLoginInfo,
            onContinue = onContinue
        )
    }

}

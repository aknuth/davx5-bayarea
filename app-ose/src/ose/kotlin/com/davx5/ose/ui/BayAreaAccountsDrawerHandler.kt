/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package com.davx5.ose.ui

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import at.bitfire.davdroid.R
import at.bitfire.davdroid.ui.AccountsDrawerHandler
import at.bitfire.davdroid.ui.AppSettingsActivity
import at.bitfire.davdroid.ui.MenuEntry
import at.bitfire.davdroid.ui.about.AboutActivity
import javax.inject.Inject

/**
 * Bay Area Sync: navigation drawer without the DAVx⁵ project links (news, donations, community, website).
 */
class BayAreaAccountsDrawerHandler @Inject constructor(): AccountsDrawerHandler() {

    @Composable
    override fun MenuEntries(
        snackbarHostState: SnackbarHostState
    ) {
        val context = LocalContext.current

        MenuEntry(
            icon = Icons.Default.Info,
            title = stringResource(R.string.navigation_drawer_about),
            onClick = {
                context.startActivity(Intent(context, AboutActivity::class.java))
            }
        )

        MenuEntry(
            icon = Icons.Default.Settings,
            title = stringResource(R.string.navigation_drawer_settings),
            onClick = {
                context.startActivity(Intent(context, AppSettingsActivity::class.java))
            }
        )
    }

}

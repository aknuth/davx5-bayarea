/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package com.davx5.ose.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import at.bitfire.davdroid.ui.AccountsDrawerHandler
import javax.inject.Inject

/**
 * Bay Area Sync: navigation drawer without the DAVx⁵ project links (news, donations, community, website) and tools (WebDAV mounts).
 */
class BayAreaAccountsDrawerHandler @Inject constructor(): AccountsDrawerHandler() {

    @Composable
    override fun MenuEntries(
        snackbarHostState: SnackbarHostState
    ) {
        // About, Settings (and beta feedback for pre-release versions)
        ImportantEntries(snackbarHostState)
    }

    override fun onBetaFeedback(
        context: Context,
        onShowSnackbar: (message: String, actionLabel: String, action: () -> Unit) -> Unit
    ) {
        // DAVx⁵ would send feedback to bitfire – not for Bay Area Sync
    }

}

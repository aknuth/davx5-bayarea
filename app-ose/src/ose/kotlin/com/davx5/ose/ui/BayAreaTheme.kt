/*
 * Copyright © All Contributors. See LICENSE and AUTHORS in the root directory for details.
 */

package com.davx5.ose.ui

import androidx.compose.ui.graphics.Color
import at.bitfire.davdroid.ui.OseTheme

/**
 * Bay Area Sync colors (taken from the app logo), based on the neutral colors of [OseTheme].
 */
object BayAreaTheme {

    private val blue = Color(0xFF1E3FE0)
    private val navy = Color(0xFF021962)
    private val lightBlue = Color(0xFF5AA0F0)
    private val paleBlue = Color(0xFFB8C7FF)
    private val palestBlue = Color(0xFFE3E9FF)

    val lightScheme = OseTheme.lightScheme.copy(
        primary = blue,
        onPrimary = Color.White,
        primaryContainer = palestBlue,
        onPrimaryContainer = navy,
        secondary = navy,
        onSecondary = Color.White,
        secondaryContainer = lightBlue,
        onSecondaryContainer = navy,
        tertiary = Color(0xFF2E6FD8),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFCFE2FA),
        onTertiaryContainer = navy,
        inversePrimary = paleBlue
    )

    val darkScheme = OseTheme.darkScheme.copy(
        primary = paleBlue,
        onPrimary = navy,
        primaryContainer = blue,
        onPrimaryContainer = palestBlue,
        secondary = Color(0xFFA9CCF7),
        onSecondary = navy,
        secondaryContainer = Color(0xFF2E6FD8),
        onSecondaryContainer = Color.White,
        tertiary = Color(0xFFA9CCF7),
        onTertiary = navy,
        tertiaryContainer = Color(0xFF2E6FD8),
        onTertiaryContainer = Color.White,
        outlineVariant = blue,
        inversePrimary = blue
    )

}

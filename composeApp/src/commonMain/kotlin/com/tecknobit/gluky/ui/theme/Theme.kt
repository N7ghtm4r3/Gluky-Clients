@file:OptIn(ExperimentalComposeRuntimeApi::class)

package com.tecknobit.gluky.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.*
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.*
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.gluky.localUser

/**
 * `lightScheme` default light colors scheme
 */
private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

/**
 * `darkScheme` default dark colors scheme
 */
private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

/**
 * Method to set the Gluky theme to the content
 *
 * @param darkTheme Whether to use the dark or light theme
 * @param content The content to display
 */
@Composable
fun GlukyTheme(
    darkTheme: Boolean = applyDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

/**
 * Method used to retrieve the custom green color to apply based on the [applyDarkTheme] result
 *
 * @return the color to apply as [Color]
 */
@Composable
fun green(): Color {
    return if (applyDarkTheme())
        GreenDark
    else
        GreenLight
}

/**
 * Method used to retrieve the custom yellow color to apply based on the [applyDarkTheme] result
 *
 * @return the color to apply as [Color]
 */
@Composable
fun yellow(): Color {
    return if (applyDarkTheme())
        YellowDark
    else
        YellowLight
}

/**
 * Method used to retrieve the custom red color to apply based on the [applyDarkTheme] result
 *
 * @return the color to apply as [Color]
 */
@Composable
fun red(): Color {
    return if (applyDarkTheme())
        RedDark
    else
        RedLight
}

/**
 * Method to check which colors scheme is to use based on the current [localUser.theme] property
 *
 * @return whether use the dark colors scheme as [Boolean]
 */
@Composable
fun applyDarkTheme(): Boolean {
    val theme by localUser.observe<ApplicationTheme>(
        key = THEME_KEY
    )
    return when (theme) {
        Dark -> true
        Light -> false
        Auto -> isSystemInDarkTheme()
    }
}

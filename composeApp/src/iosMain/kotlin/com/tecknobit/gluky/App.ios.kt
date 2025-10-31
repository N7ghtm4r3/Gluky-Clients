package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.delay
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    LaunchedEffect(Unit) {
        delay(1000)
        startSession()
    }
}

/**
 * Method to points locale language for the application
 */
actual fun setUserLanguage() {
    val locale = NSLocale(
        localeIdentifier = localUser.language
    )
    NSUserDefaults.standardUserDefaults.setObject(
        value = locale,
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()
}

/**
 * Method to manage correctly the back navigation from the current screen
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
}
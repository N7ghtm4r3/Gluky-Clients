package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.utilities.getCurrentLocaleLanguage
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
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
    // TODO: TO REMOVE THIS WORKAROUND AND USE DIRECTLY localUser.language
    val language = localUser.language.ifBlank {
        val currentLocaleLang = getCurrentLocaleLanguage()
        if (LANGUAGES_SUPPORTED.containsKey(currentLocaleLang))
            currentLocaleLang
        else
            DEFAULT_LANGUAGE
    }
    val locale = NSLocale(
        localeIdentifier = language
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
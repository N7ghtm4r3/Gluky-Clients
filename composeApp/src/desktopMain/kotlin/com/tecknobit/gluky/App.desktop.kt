package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
actual fun CheckForUpdatesAndLaunch() {
    // TODO: TO SET
    /*GliderTheme {
        var launchApp by remember { mutableStateOf(true) }
        UpdaterDialog(
            config = OctocatKDUConfig(
                locale = Locale.getDefault(),
                appName = stringResource(Res.string.app_name),
                currentVersion = stringResource(Res.string.app_version),
                onUpdateAvailable = { launchApp = false },
                dismissAction = { launchApp = true }
            )
        )
        if (launchApp)
            startSession()
    }*/

    startSession()
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    // TODO: TO SET
    // Locale.setDefault(Locale.forLanguageTag(localUser.language))
}
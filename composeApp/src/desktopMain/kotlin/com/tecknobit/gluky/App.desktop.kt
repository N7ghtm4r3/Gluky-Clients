package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tecknobit.equinoxcompose.utilities.getCurrentLocaleLanguage
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.gluky.ui.theme.GlukyTheme
import com.tecknobit.octocatkdu.OctocatKDUConfig
import com.tecknobit.octocatkdu.UpdaterDialog
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.app_name
import gluky.composeapp.generated.resources.app_version
import org.jetbrains.compose.resources.stringResource
import java.util.Locale

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 */
@Composable
actual fun CheckForUpdatesAndLaunch() {
    GlukyTheme {
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
    }
}

/**
 * Method to manage correctly the back navigation from the current screen
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
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
    Locale.setDefault(Locale.forLanguageTag(language))
}
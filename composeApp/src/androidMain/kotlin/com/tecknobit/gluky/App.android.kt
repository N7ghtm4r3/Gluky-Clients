package com.tecknobit.gluky

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.equinoxcompose.utilities.getCurrentLocaleLanguage
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcore.utilities.AppContext
import com.tecknobit.gluky.MainActivity.Companion.appUpdateManager
import com.tecknobit.gluky.MainActivity.Companion.launcher
import moe.tlaster.precompose.navigation.BackHandler
import java.util.Locale

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
        val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
        val isUpdateSupported = info.isImmediateUpdateAllowed
        if (isUpdateAvailable && isUpdateSupported) {
            appUpdateManager.startUpdateFlowForResult(
                info,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } else
            startSession()
    }.addOnFailureListener {
        startSession()
    }
}

/**
 * Method to manage correctly the back navigation from the current screen
 */
@Composable
actual fun CloseApplicationOnNavBack() {
    val activity = LocalActivity.current
    BackHandler {
        activity?.finishAffinity()
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
    val locale = Locale(language)
    Locale.setDefault(locale)
    val context = AppContext.get()
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}
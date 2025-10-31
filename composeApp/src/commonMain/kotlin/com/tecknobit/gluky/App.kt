package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcompose.session.screens.equinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.gluky.GlukyConfig.LOCAL_STORAGE_PATH
import com.tecknobit.gluky.helpers.AUTH_SCREEN
import com.tecknobit.gluky.helpers.GlukyRequester
import com.tecknobit.gluky.helpers.HOME_SCREEN
import com.tecknobit.gluky.helpers.SPLASHSCREEN
import com.tecknobit.gluky.helpers.customHttpClient
import com.tecknobit.gluky.helpers.navToAuthScreen
import com.tecknobit.gluky.helpers.navToSplashscreen
import com.tecknobit.gluky.helpers.navigator
import com.tecknobit.gluky.ui.components.imageLoader
import com.tecknobit.gluky.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.gluky.ui.screens.home.HomeScreen
import com.tecknobit.gluky.ui.screens.splashscreen.Splashscreen
import com.tecknobit.gluky.ui.theme.GlukyTheme
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.comicneue
import gluky.composeapp.generated.resources.fredoka
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font

/**
 * `displayFontFamily` the Gluky's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * `bodyFontFamily` the Gluky's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * `requester` the instance to manage the requests with the backend
 */
lateinit var requester: GlukyRequester

/**
 * `localUser` the local user session details
 */
val localUser = EquinoxLocalUser(
    localStoragePath = LOCAL_STORAGE_PATH,
    observableKeys = setOf(THEME_KEY)
)

/**
 * Common entry point of the `Gluky` application
 */
@Composable
fun App() {
    displayFontFamily = FontFamily(Font(Res.font.fredoka))
    bodyFontFamily = FontFamily(Font(Res.font.comicneue))
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = customHttpClient()
                )
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    navigator = rememberNavController()
    GlukyTheme {
        NavHost(
            navController = navigator,
            startDestination = SPLASHSCREEN
        ) {
            composable(
                route = SPLASHSCREEN
            ) {
                val splashscreen = equinoxScreen { Splashscreen() }
                splashscreen.ShowContent()
            }
            composable(
                route = AUTH_SCREEN
            ) {
                val authScreen = equinoxScreen { AuthScreen() }
                authScreen.ShowContent()
            }
            composable(
                route = HOME_SCREEN
            ) {
                val homeScreen = equinoxScreen { HomeScreen() }
                homeScreen.ShowContent()
            }
        }
    }
    SessionFlowState.invokeOnUserDisconnected {
        localUser.clear()
        navToSplashscreen()
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 */
fun startSession() {
    requester = GlukyRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    setUserLanguage()
    val route = if (localUser.isAuthenticated) {
        MainScope().launch {
            requester.sendRequest(
                request = { getDynamicAccountData() },
                onSuccess = { response ->
                    localUser.updateDynamicAccountData(
                        dynamicData = response.toResponseData()
                    )
                    setUserLanguage()
                },
                onFailure = {
                    localUser.clear()
                    requester.clearSession()
                    navToAuthScreen()
                },
                onConnectionError = { }
            )
        }
        HOME_SCREEN
    } else
        AUTH_SCREEN
    navigator.navigate(route)
}

/**
 * Method to points locale language for the application
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 */
@Composable
expect fun CloseApplicationOnNavBack()
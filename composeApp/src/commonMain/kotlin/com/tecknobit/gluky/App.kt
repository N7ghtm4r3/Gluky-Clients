package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.gluky.helpers.GlukyRequester
import com.tecknobit.gluky.helpers.customHttpClient
import com.tecknobit.gluky.ui.components.imageLoader
import com.tecknobit.gluky.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.gluky.ui.screens.home.HomeScreen
import com.tecknobit.gluky.ui.screens.splashscreen.Splashscreen
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.comicneue
import gluky.composeapp.generated.resources.fredoka
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
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
 * `navigator` the instance used to navigate between the screens of the app
 */
lateinit var navigator: Navigator

/**
 * `requester` the instance to manage the requests with the backend
 */
lateinit var requester: GlukyRequester

/**
 * `SPLASHSCREEN` the route to navigate to the [com.tecknobit.gluky.ui.screens.splashscreen.Splashscreen]
 */
const val SPLASHSCREEN = "SplashScreen"

/**
 * `AUTH_SCREEN` the route to navigate to the [com.tecknobit.gluky.ui.screens.auth.presenter.AuthScreen]
 */
const val AUTH_SCREEN = "AuthScreen"

/**
 * `HOME_SCREEN` the route to navigate to the [com.tecknobit.gluky.ui.screens.home.HomeScreen]
 */
const val HOME_SCREEN = "HomeScreen"

/**
 * `localUser` the local user session details
 */
val localUser = EquinoxLocalUser(
    localStoragePath = "Gluky"
)

// TODO: TO SET AMETISTA
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
                // TODO: CHECK TO KEEP OR TO REMOVE
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
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASHSCREEN
        ) {
            scene(
                route = SPLASHSCREEN
            ) {
                Splashscreen().ShowContent()
            }
            scene(
                route = AUTH_SCREEN
            ) {
                AuthScreen().ShowContent()
            }
            scene(
                route = HOME_SCREEN
            ) {
                HomeScreen().ShowContent()
            }
        }
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
        userId = localUser.userId,
        userToken = localUser.userToken,
        debugMode = true // TODO: TO REMOVE
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
                    navigator.navigate(AUTH_SCREEN)
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
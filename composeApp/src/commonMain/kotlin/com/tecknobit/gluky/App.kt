package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.gluky.helpers.customHttpClient
import com.tecknobit.gluky.ui.components.imageLoader
import com.tecknobit.gluky.ui.screens.HomeScreen
import com.tecknobit.gluky.ui.screens.Splashscreen
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.comicneue
import gluky.composeapp.generated.resources.fredoka
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

lateinit var navigator: Navigator

const val SPLASHSCREEN = "SPLASHSCREEN"

const val HOME_SCREEN = "HomeScreen"

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
 *
 */
fun startSession() {
    // TODO: TO SET
//    requester = GliderRequester(
//        host = localUser.hostAddress,
//        userId = localUser.userId,
//        userToken = localUser.userToken,
//        deviceId = localUser.deviceId
//    )
//    setUserLanguage()
//    val route = if (localUser.isAuthenticated) {
//        MainScope().launch {
//            requester.sendRequest(
//                request = { getCustomDynamicAccountData() },
//                onSuccess = { response ->
//                    localUser.updateDynamicAccountData(
//                        dynamicData = response.toResponseData()
//                    )
//                    setUserLanguage()
//                },
//                onFailure = {
//                    localUser.clear()
//                    requester.clearSession()
//                    navigator.navigate(AUTH_SCREEN)
//                },
//                onConnectionError = { }
//            )
//        }
//        HOME_SCREEN
//    } else
//        AUTH_SCREEN
    navigator.navigate(HOME_SCREEN)
}

/**
 * Method to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
expect fun CloseApplicationOnNavBack()
package com.tecknobit.gluky.helpers

import androidx.navigation.NavHostController
import com.tecknobit.equinoxcompose.annotations.DestinationScreen
import com.tecknobit.gluky.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.gluky.ui.screens.home.HomeScreen
import com.tecknobit.gluky.ui.screens.splashscreen.Splashscreen

/**
 * `navigator` the instance used to navigate between the screens of the app
 */
lateinit var navigator: NavHostController

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
 * Method used to navigate to the [Splashscreen]
 *
 * @since 1.0.2
 */
@DestinationScreen(Splashscreen::class)
fun navToSplashscreen() {
    navigator.navigate(SPLASHSCREEN)
}

/**
 * Method used to navigate to the [AuthScreen]
 *
 * @since 1.0.2
 */
@DestinationScreen(AuthScreen::class)
fun navToAuthScreen() {
    navigator.navigate(AUTH_SCREEN)
}

/**
 * Method used to navigate to the [HomeScreen]
 *
 * @since 1.0.2
 */
@DestinationScreen(HomeScreen::class)
fun navToHomeScreen() {
    navigator.navigate(HOME_SCREEN)
}
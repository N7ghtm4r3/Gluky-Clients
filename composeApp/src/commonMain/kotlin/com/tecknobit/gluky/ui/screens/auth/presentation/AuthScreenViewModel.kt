package com.tecknobit.gluky.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.gluky.HOME_SCREEN
import com.tecknobit.gluky.helpers.GlukyRequester
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.navigator
import kotlinx.serialization.json.JsonObject

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = GlukyRequester(), // TODO: TO SET CORRECTLY
    localUser = localUser
) {

    // TODO: TO REMOVE
    fun fakeAuth() {
        navigator.navigate(HOME_SCREEN)
    }

}
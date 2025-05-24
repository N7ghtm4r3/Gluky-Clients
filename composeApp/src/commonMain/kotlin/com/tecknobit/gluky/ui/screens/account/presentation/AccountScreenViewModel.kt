package com.tecknobit.gluky.ui.screens.account.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.gluky.helpers.GlukyRequester
import com.tecknobit.gluky.localUser

class AccountScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = GlukyRequester(), // TODO: TO SET CORRECTLY
    localUser = localUser // TODO: TO SET CORRECTLY
)
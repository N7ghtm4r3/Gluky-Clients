package com.tecknobit.gluky.ui.screens.account.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.requester

class AccountScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
)
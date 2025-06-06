package com.tecknobit.gluky.ui.screens.account.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.requester

/**
 * The `AccountScreenViewModel` class is the support class used to change the user account settings
 * or preferences
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see EquinoxProfileViewModel
 */
class AccountScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
)
package com.tecknobit.gluky.ui.screens.account.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.gluky.ui.screens.account.presentation.AccountScreenViewModel

class AccountScreen : EquinoxScreen<AccountScreenViewModel>(
    viewModel = AccountScreenViewModel()
) {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}
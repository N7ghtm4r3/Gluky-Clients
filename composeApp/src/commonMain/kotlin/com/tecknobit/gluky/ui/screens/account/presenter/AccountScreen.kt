package com.tecknobit.gluky.ui.screens.account.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.tecknobit.gluky.ui.screens.account.presentation.AccountScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage

class AccountScreen : GlukyScreenPage<AccountScreenViewModel>(
    viewModel = AccountScreenViewModel()
) {

    @Composable
    override fun ColumnScope.ScreenPageContent() {
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}
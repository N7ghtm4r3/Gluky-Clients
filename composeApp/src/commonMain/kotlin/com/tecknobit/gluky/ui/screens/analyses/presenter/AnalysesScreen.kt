package com.tecknobit.gluky.ui.screens.analyses.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage

class AnalysesScreen : GlukyScreenPage<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel()
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
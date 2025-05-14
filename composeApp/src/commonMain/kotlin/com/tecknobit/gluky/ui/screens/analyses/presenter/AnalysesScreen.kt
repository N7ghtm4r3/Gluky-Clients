package com.tecknobit.gluky.ui.screens.analyses.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel

class AnalysesScreen : EquinoxScreen<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel()
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
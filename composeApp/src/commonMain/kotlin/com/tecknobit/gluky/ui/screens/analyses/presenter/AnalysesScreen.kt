@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.analyses.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.gluky.ui.screens.analyses.components.GlycemiaTrend
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.analyses

class AnalysesScreen : GlukyScreenPage<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel(),
    title = Res.string.analyses
) {

    private lateinit var glycemiaTrendData: State<GlycemiaTrendData?>

    @Composable
    override fun ColumnScope.ScreenPageContent() {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            initialLoadingRoutineDelay = 2000,
            loadingRoutine = { glycemiaTrendData.value != null },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                GlycemiaTrend(
                    viewModel = viewModel,
                    glycemiaTrendData = glycemiaTrendData.value!!
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveGlycemiaTrendData()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.sessionFlowState = rememberSessionFlowState()
        glycemiaTrendData = viewModel.glycemiaTrendData.collectAsState()
    }

}
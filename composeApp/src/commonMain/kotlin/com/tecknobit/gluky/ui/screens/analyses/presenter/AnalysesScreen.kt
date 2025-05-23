@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.analyses.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.gluky.ui.screens.analyses.components.CustomPeriodButton
import com.tecknobit.gluky.ui.screens.analyses.components.GlycemicTrend
import com.tecknobit.gluky.ui.screens.analyses.components.GroupingDayChip
import com.tecknobit.gluky.ui.screens.analyses.components.TrendPeriodChip
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.analyses

class AnalysesScreen : GlukyScreenPage<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel(),
    title = Res.string.analyses
) {

    private lateinit var glycemicTrendData: State<GlycemicTrendData?>

    private lateinit var glycemicTrendPeriod: State<GlycemicTrendPeriod>

    private lateinit var glycemicTrendGroupingDay: State<GlycemicTrendGroupingDay?>

    @Composable
    override fun ColumnScope.ScreenPageContent() {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            initialLoadingRoutineDelay = 2000,
            loadingRoutine = { glycemicTrendData.value != null },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedVisibility(
                        visible = glycemicTrendData.value!!.sets.isNotEmpty()
                    ) {
                        FiltersSection()
                    }
                    ChartsSection()
                }
            }
        )
    }

    @Composable
    @ScreenSection
    private fun FiltersSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GroupingDayChip(
                viewModel = viewModel,
                groupingDay = glycemicTrendGroupingDay.value
            )
            TrendPeriodChip(
                viewModel = viewModel,
                trendPeriod = glycemicTrendPeriod.value
            )
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                CustomPeriodButton(
                    viewModel = viewModel
                )
            }
        }
    }

    @Composable
    @ScreenSection
    private fun ChartsSection() {
        GlycemicTrend(
            viewModel = viewModel,
            glycemicTrendData = glycemicTrendData.value!!,
            glycemicTrendPeriod = glycemicTrendPeriod.value,
            glycemicTrendGroupingDay = glycemicTrendGroupingDay.value
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
        glycemicTrendData = viewModel.glycemicTrendData.collectAsState()
        glycemicTrendPeriod = viewModel.glycemicTrendPeriod.collectAsState()
        glycemicTrendGroupingDay = viewModel.glycemicTrendGroupingDay.collectAsState()
    }

}
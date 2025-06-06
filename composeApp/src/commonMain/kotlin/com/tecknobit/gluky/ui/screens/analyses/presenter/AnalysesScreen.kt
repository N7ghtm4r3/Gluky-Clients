@file:OptIn(ExperimentalComposeApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.presenter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.awaitNullItemLoaded
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.components.RetryButton
import com.tecknobit.gluky.ui.icons.Report
import com.tecknobit.gluky.ui.screens.analyses.components.CustomPeriodButton
import com.tecknobit.gluky.ui.screens.analyses.components.EmptyTrendData
import com.tecknobit.gluky.ui.screens.analyses.components.GlycemicReportDialog
import com.tecknobit.gluky.ui.screens.analyses.components.GlycemicTrend
import com.tecknobit.gluky.ui.screens.analyses.components.GroupingDayChip
import com.tecknobit.gluky.ui.screens.analyses.components.TrendPeriodChip
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenTab
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.analyses
import gluky.composeapp.generated.resources.create_report
import org.jetbrains.compose.resources.stringResource

class AnalysesScreen : GlukyScreenTab<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel(),
    title = Res.string.analyses
) {

    private lateinit var glycemicTrend: State<GlycemicTrendDataContainer?>

    private lateinit var glycemicTrendPeriod: State<GlycemicTrendPeriod>

    private lateinit var glycemicTrendGroupingDay: State<GlycemicTrendGroupingDay?>

    @Composable
    override fun ColumnScope.ScreenContent() {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            initialLoadingRoutineDelay = 1000,
            loadingRoutine = { glycemicTrend.value != null },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                CollectStatesAfterLoading()
                AnimatedContent(
                    targetState = glycemicTrend.value!!.dataAvailable(),
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) }
                ) { dataAvailable ->
                    if (dataAvailable) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FiltersSection()
                            ChartsSection()
                        }
                    } else {
                        EmptyTrendData(
                            viewModel = viewModel,
                            glycemicTrendPeriod = glycemicTrendPeriod.value
                        )
                    }
                }
            },
            retryFailedFlowContent = {
                RetryButton(
                    retryAction = {
                        viewModel.retrieveGlycemicTrend()
                    }
                )
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
                    viewModel = viewModel,
                    glycemicTrendData = glycemicTrend.value!!,
                    trendPeriod = glycemicTrendPeriod.value
                )
            }
        }
    }

    @Composable
    @ScreenSection
    private fun ChartsSection() {
        LazyColumn(
            modifier = Modifier
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = glycemicTrend.value!!.availableSets,
                key = { type -> type }
            ) { type ->
                val glycemicTrendSet = glycemicTrend.value!!.getRelatedSet(type)
                glycemicTrendSet?.let { trendSet ->
                    if (trendSet.hasDataAvailable()) {
                        GlycemicTrend(
                            type = type,
                            glycemicTrendDataContainer = glycemicTrend.value!!,
                            glycemicTrendData = trendSet,
                            glycemicTrendPeriod = glycemicTrendPeriod.value,
                            glycemicTrendGroupingDay = glycemicTrendGroupingDay.value
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun FABContent() {
        awaitNullItemLoaded(
            itemToWait = glycemicTrend.value,
            extras = { glycemicTrendData -> glycemicTrendData.dataAvailable() }
        ) {
            val createReportTitle = stringResource(Res.string.create_report)
            val createReport = remember { mutableStateOf(false) }
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                expanded = responsiveAssignment(
                    onExpandedSizeClass = { true },
                    onMediumSizeClass = { false },
                    onCompactSizeClass = { false }
                ),
                icon = {
                    Icon(
                        imageVector = Report,
                        contentDescription = createReportTitle
                    )
                },
                text = {
                    Text(
                        text = createReportTitle
                    )
                },
                onClick = { createReport.value = !createReport.value }
            )
            GlycemicReportDialog(
                show = createReport,
                viewModel = viewModel
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveGlycemicTrend()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.sessionFlowState = rememberSessionFlowState()
        glycemicTrend = viewModel.glycemicTrend.collectAsState()
        glycemicTrendPeriod = viewModel.glycemicTrendPeriod.collectAsState()
        glycemicTrendGroupingDay = viewModel.glycemicTrendGroupingDay.collectAsState()
    }

    @Composable
    override fun CollectStatesAfterLoading() {
        viewModel.customPeriodPickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = glycemicTrend.value!!.from,
            initialSelectedEndDateMillis = glycemicTrend.value!!.to
        )
    }

}
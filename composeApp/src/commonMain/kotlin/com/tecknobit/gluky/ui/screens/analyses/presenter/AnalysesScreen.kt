@file:OptIn(ExperimentalComposeApi::class)

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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.tecknobit.equinoxcompose.utilities.awaitNullItemLoaded
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.icons.Report
import com.tecknobit.gluky.ui.screens.analyses.components.CustomPeriodButton
import com.tecknobit.gluky.ui.screens.analyses.components.EmptyTrendData
import com.tecknobit.gluky.ui.screens.analyses.components.GlycemicTrend
import com.tecknobit.gluky.ui.screens.analyses.components.GroupingDayChip
import com.tecknobit.gluky.ui.screens.analyses.components.TrendPeriodChip
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.MeasurementType
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.analyses
import gluky.composeapp.generated.resources.create_report
import org.jetbrains.compose.resources.stringResource

class AnalysesScreen : GlukyScreenPage<AnalysesScreenViewModel>(
    viewModel = AnalysesScreenViewModel(),
    title = Res.string.analyses
) {

    private lateinit var glycemicTrendData: State<GlycemicTrendDataContainer?>

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
                AnimatedContent(
                    targetState = glycemicTrendData.value!!.dataAvailable(),
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
                    glycemicTrendData = glycemicTrendData.value!!,
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
                items = MeasurementType.entries.toList(),
                key = { type -> type }
            ) { type ->
                val glycemicTrendSet = glycemicTrendData.value!!.getRelatedSet(type)
                glycemicTrendSet?.let { trendSet ->
                    GlycemicTrend(
                        type = type,
                        glycemicTrendData = trendSet,
                        glycemicTrendPeriod = glycemicTrendPeriod.value,
                        glycemicTrendGroupingDay = glycemicTrendGroupingDay.value
                    )
                }
            }
        }
    }

    @Composable
    override fun FABContent() {
        awaitNullItemLoaded(
            itemToWait = glycemicTrendData.value
        ) {
            val createReport = stringResource(Res.string.create_report)
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
                        contentDescription = createReport
                    )
                },
                text = {
                    Text(
                        text = createReport
                    )
                },
                onClick = { viewModel.createReport() }
            )
        }
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
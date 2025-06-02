package com.tecknobit.gluky.ui.screens.measurements.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.resources.retry
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.measurements.components.DailyNotes
import com.tecknobit.gluky.ui.screens.measurements.components.Measurements
import com.tecknobit.gluky.ui.screens.measurements.components.UnfilledDay
import com.tecknobit.gluky.ui.screens.measurements.components.daypickers.DayPickerBar
import com.tecknobit.gluky.ui.screens.measurements.components.daypickers.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.daily_notes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeApi::class, ExperimentalMaterial3Api::class)
class MeasurementsScreen : GlukyScreenPage<MeasurementsScreenViewModel>(
    viewModel = MeasurementsScreenViewModel(),
    useResponsiveWidth = false
) {

    private lateinit var currentDay: State<Long>

    private lateinit var dailyMeasurements: State<DailyMeasurements?>

    @Composable
    @LayoutCoordinator
    override fun ColumnScope.ScreenPageContent() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResponsiveContent(
                onExpandedSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = { ScreenContent() }
                    )
                },
                onMediumSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = { ScreenContent() }
                    )
                },
                onMediumWidthExpandedHeight = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = {
                            ScreenContent(
                                horizontalPadding = 16.dp
                            )
                        }
                    )
                },
                onCompactSizeClass = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = {
                            ScreenContent(
                                horizontalPadding = 16.dp
                            )
                        }
                    )
                }
            )
        }
    }

    @Composable
    private fun ScreenContent(
        horizontalPadding: Dp = 0.dp,
    ) {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            loadingRoutine = { true },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                AnimatedVisibility(
                    visible = dailyMeasurements.value == null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    UnfilledDay(
                        viewModel = viewModel
                    )
                }
                AnimatedVisibility(
                    visible = dailyMeasurements.value != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    dailyMeasurements.value?.let {
                        Measurements(
                            viewModel = viewModel,
                            horizontalPadding = horizontalPadding,
                            dailyMeasurements = dailyMeasurements.value!!
                        )
                    }
                }
            },
            retryFailedFlowContent = {
                TextButton(
                    onClick = { viewModel.retrieveDailyMeasurements() }
                ) {
                    Text(
                        text = stringResource(com.tecknobit.equinoxcompose.resources.Res.string.retry)
                    )
                }
            }
        )
    }

    @Composable
    override fun FABContent() {
        AnimatedVisibility(
            visible = dailyMeasurements.value != null
        ) {
            dailyMeasurements.value?.let {
                val dailyNotes = stringResource(Res.string.daily_notes)
                val state = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                    confirmValueChange = { false }
                )
                val scope = rememberCoroutineScope()
                ExtendedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    expanded = responsiveAssignment(
                        onExpandedSizeClass = { true },
                        onMediumSizeClass = { false },
                        onCompactSizeClass = { false }
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.NoteAlt,
                            contentDescription = dailyNotes
                        )
                    },
                    text = {
                        Text(
                            text = dailyNotes
                        )
                    },
                    onClick = {
                        scope.launch {
                            state.show()
                        }
                    }
                )
                DailyNotes(
                    state = state,
                    scope = scope,
                    viewModel = viewModel,
                    mealDay = dailyMeasurements.value!!
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveDailyMeasurements()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentDay = viewModel.currentDay.collectAsState()
        dailyMeasurements = viewModel.dailyMeasurements.collectAsState()
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}
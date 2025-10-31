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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.components.RetryButton
import com.tecknobit.gluky.ui.screens.measurements.components.DailyNotes
import com.tecknobit.gluky.ui.screens.measurements.components.Measurements
import com.tecknobit.gluky.ui.screens.measurements.components.UnfilledDay
import com.tecknobit.gluky.ui.screens.measurements.components.daypickers.DayPickerBar
import com.tecknobit.gluky.ui.screens.measurements.components.daypickers.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenTab
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.daily_notes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * The [MeasurementsScreen] displays the measurements of a day based on the day selected by a dedicated
 * picker
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see GlukyScreenTab
 */
@OptIn(ExperimentalMaterial3Api::class)
class MeasurementsScreen : GlukyScreenTab<MeasurementsScreenViewModel>(
    viewModel = MeasurementsScreenViewModel(),
    useResponsiveWidth = false
) {

    /**
     * `currentDay` the current day selected
     */
    private lateinit var currentDay: State<Long>

    /**
     * `dailyMeasurements` the daily measurements related to the selected [currentDay]
     */
    private lateinit var dailyMeasurements: State<DailyMeasurements?>

    /**
     * The custom content of the tab
     */
    @Composable
    @LayoutCoordinator
    override fun ColumnScope.TabContent() {
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
                        content = { MeasurementsContent() }
                    )
                },
                onMediumSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = { MeasurementsContent() }
                    )
                },
                onMediumWidthExpandedHeight = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        content = {
                            MeasurementsContent(
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
                            MeasurementsContent(
                                horizontalPadding = 16.dp
                            )
                        }
                    )
                }
            )
        }
    }

    /**
     * Content related to the measurements
     *
     * @param horizontalPadding The padding to apply horizontally between the items in the list
     */
    @Composable
    private fun MeasurementsContent(
        horizontalPadding: Dp = 0.dp,
    ) {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            initialLoadingRoutineDelay = 1000,
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
                RetryButton(
                    retryAction = {
                        viewModel.retrieveDailyMeasurements()
                    }
                )
            }
        )
    }

    /**
     * The content displayed in the `Scaffold.floatingActionButton` section
     */
    @Composable
    override fun FABContent() {
        AnimatedVisibility(
            visible = dailyMeasurements.value != null
        ) {
            dailyMeasurements.value?.let {
                val dailyNotes = stringResource(Res.string.daily_notes)
                val state = rememberModalBottomSheetState()
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
                    dailyMeasurements = dailyMeasurements.value!!
                )
            }
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
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
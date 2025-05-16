package com.tecknobit.gluky.ui.screens.meals.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.gluky.ui.screens.meals.components.DayPickerBar
import com.tecknobit.gluky.ui.screens.meals.components.MealDay
import com.tecknobit.gluky.ui.screens.meals.components.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.GlukyScreenPage

@OptIn(ExperimentalComposeApi::class)
class MealsScreen : GlukyScreenPage<MealsScreenViewModel>(
    viewModel = MealsScreenViewModel(),
    useResponsiveWidth = false
) {

    private lateinit var currentDay: State<Long>

    private lateinit var mealDay: State<MealDayData?>

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
                        mealContent = { MealDataContent() }
                    )
                },
                onMediumSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = { MealDataContent() }
                    )
                },
                onMediumWidthExpandedHeight = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDataContent(
                                horizontalPadding = 16.dp
                            )
                        }
                    )
                },
                onCompactSizeClass = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDataContent(
                                horizontalPadding = 16.dp
                            )
                        }
                    )
                }
            )
        }
    }

    @Composable
    @ScreenSection
    private fun MealDataContent(
        horizontalPadding: Dp = 0.dp,
    ) {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            initialLoadingRoutineDelay = 1000,
            loadingRoutine = { mealDay.value != null },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                MealDay(
                    viewModel = viewModel,
                    horizontalPadding = horizontalPadding,
                    mealDay = mealDay.value!!
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveMealDay()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentDay = viewModel.currentDay.collectAsState()
        mealDay = viewModel.mealDay.collectAsState()
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}
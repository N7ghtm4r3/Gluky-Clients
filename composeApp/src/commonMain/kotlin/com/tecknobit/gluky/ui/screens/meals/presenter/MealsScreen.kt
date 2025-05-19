package com.tecknobit.gluky.ui.screens.meals.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.meals.components.DayPickerBar
import com.tecknobit.gluky.ui.screens.meals.components.MealDay
import com.tecknobit.gluky.ui.screens.meals.components.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.GlukyScreenPage
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.my_meals

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
            initialLoadingRoutineDelay = 2000,
            loadingRoutine = { true },
            loadingContentColor = MaterialTheme.colorScheme.primary,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                ) {
                    MyMeals(
                        horizontalPadding = horizontalPadding
                    )
                }
            }
        )
    }

    @Composable
    @ScreenSection
    private fun MyMeals(
        horizontalPadding: Dp = 0.dp,
    ) {
        SectionTitle(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp
                ),
            title = Res.string.my_meals,
            style = AppTypography.titleLarge
        )
        MealDay(
            viewModel = viewModel,
            horizontalPadding = horizontalPadding,
            mealDay = mealDay.value
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
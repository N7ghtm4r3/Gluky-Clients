package com.tecknobit.gluky.ui.screens.meals.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.gluky.ui.screens.meals.components.DayPickerBar
import com.tecknobit.gluky.ui.screens.meals.components.MealDay
import com.tecknobit.gluky.ui.screens.meals.components.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.GlukyScreenPage

class MealsScreen : GlukyScreenPage<MealsScreenViewModel>(
    viewModel = MealsScreenViewModel(),
    useResponsiveWidth = false
) {

    private lateinit var currentDay: State<Long>

    @Composable
    @LayoutCoordinator
    override fun ColumnScope.ScreenPageContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ResponsiveContent(
                onExpandedSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDay(
                                viewModel = viewModel
                            )
                        }
                    )
                },
                onMediumSizeClass = {
                    DayPickerBar(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDay(
                                viewModel = viewModel
                            )
                        }
                    )
                },
                onMediumWidthExpandedHeight = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDay(
                                viewModel = viewModel
                            )
                        }
                    )
                },
                onCompactSizeClass = {
                    ScrollableDayPicker(
                        viewModel = viewModel,
                        currentDay = currentDay.value,
                        mealContent = {
                            MealDay(
                                viewModel = viewModel
                            )
                        }
                    )
                }
            )
        }
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentDay = viewModel.currentDay.collectAsState()
    }

}
package com.tecknobit.gluky.ui.screens.meals.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.gluky.ui.screens.meals.components.ScrollableDayPicker
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel

class MealsScreen : EquinoxScreen<MealsScreenViewModel>(
    viewModel = MealsScreenViewModel()
) {

    private lateinit var day: State<Long>

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    @LayoutCoordinator
    override fun ArrangeScreenContent() {
        ResponsiveContent(
            onExpandedSizeClass = {

            },
            onMediumSizeClass = {
            },
            onMediumWidthExpandedHeight = {
                ScrollableDayPicker(
                    viewModel = viewModel,
                    day = day.value
                )
            },
            onCompactSizeClass = {
                ScrollableDayPicker(
                    viewModel = viewModel,
                    day = day.value
                )
            }
        )
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        day = viewModel.day.collectAsState()
    }

}
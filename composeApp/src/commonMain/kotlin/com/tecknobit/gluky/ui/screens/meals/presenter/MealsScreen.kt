package com.tecknobit.gluky.ui.screens.meals.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.gluky.ui.screens.meals.components.MealPicker
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel

class MealsScreen : EquinoxScreen<MealsScreenViewModel>(
    viewModel = MealsScreenViewModel()
) {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        MealPicker(
            viewModel = viewModel
        )
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}
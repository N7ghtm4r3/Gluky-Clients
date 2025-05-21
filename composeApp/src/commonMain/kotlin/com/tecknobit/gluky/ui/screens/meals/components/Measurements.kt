package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.gluky.ui.screens.meals.components.measurementcards.BasalInsulinCard
import com.tecknobit.gluky.ui.screens.meals.components.measurementcards.MealCard
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.glukycore.enums.MeasurementType

@Composable
@ScreenSection
fun Measurements(
    viewModel: MealsScreenViewModel,
    horizontalPadding: Dp = 0.dp,
    mealDay: MealDayData,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .height(500.dp)
            .animateContentSize(),
        columns = StaggeredGridCells.Adaptive(
            minSize = 350.dp
        ),
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = horizontalPadding
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {
        items(
            items = MeasurementType.meals(),
            key = { type -> type }
        ) { type ->
            MealCard(
                viewModel = viewModel,
                meal = mealDay.getMealByType(
                    type = type
                )
            )
        }
        item {
            BasalInsulinCard(
                viewModel = viewModel,
                mealDay = mealDay
            )
        }
    }
}

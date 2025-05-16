package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.glukycore.enums.MeasurementType

@Composable
fun MealDay(
    viewModel: MealsScreenViewModel,
    horizontalPadding: Dp = 0.dp,
    mealDay: MealDayData,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .navigationBarsPadding(),
        columns = GridCells.Adaptive(
            minSize = 350.dp
        ),
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = horizontalPadding
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
    }
}


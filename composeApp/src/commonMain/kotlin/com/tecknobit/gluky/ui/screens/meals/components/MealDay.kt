@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.glukycore.MeasurementType

@Composable
fun MealDay(
    viewModel: MealsScreenViewModel,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .responsiveMaxWidth(),
        columns = GridCells.Adaptive(
            minSize = 400.dp
        ),
        contentPadding = PaddingValues(
            vertical = 32.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = MeasurementType.meals(),
            key = { mealType -> mealType }
        ) { mealType ->
            MealCard(
                viewModel = viewModel,
                mealType = mealType
            )
        }
    }
}


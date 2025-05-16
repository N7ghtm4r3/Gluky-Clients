package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.glukycore.MeasurementType

@Composable
fun MealCard(
    viewModel: MealsScreenViewModel,
    mealType: MeasurementType,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Text(
            text = mealType.name
        )
    }
}
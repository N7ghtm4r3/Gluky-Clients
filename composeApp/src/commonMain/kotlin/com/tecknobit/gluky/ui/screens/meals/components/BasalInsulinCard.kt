package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.GlukyCardColors

@Composable
fun BasalInsulinCard(
    viewModel: MealsScreenViewModel,
    mealDay: MealDayData,
    horizontalPadding: Dp,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(
                max = 200.dp
            ),
        columns = GridCells.Adaptive(
            minSize = 350.dp
        )
    ) {
        item {
            BasalInsulinCardImpl(
                viewModel = viewModel,
                mealDay = mealDay,
                horizontalPadding = horizontalPadding
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun BasalInsulinCardImpl(
    horizontalPadding: Dp,
    viewModel: MealsScreenViewModel,
    mealDay: MealDayData,
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = horizontalPadding
            )
            .fillMaxWidth()
            .height(200.dp),
        colors = GlukyCardColors
    ) {
        Text("t")
    }
}

@Composable
private fun CardHeader() {

}
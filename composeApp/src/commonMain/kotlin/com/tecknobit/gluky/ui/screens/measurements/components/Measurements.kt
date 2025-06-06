package com.tecknobit.gluky.ui.screens.measurements.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.gluky.ui.screens.measurements.components.measurementcards.BasalInsulinCard
import com.tecknobit.gluky.ui.screens.measurements.components.measurementcards.MealCard
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.glukycore.enums.MeasurementType

/**
 * Section of the screen where are displayed the measurement for a specific day
 *
 * @param viewModel The support viewmodel of the screen
 * @param horizontalPadding The padding to apply horizontally between the items in the list
 * @param dailyMeasurements The container with the measurement of a specific day
 */
@Composable
@ScreenSection
fun Measurements(
    viewModel: MeasurementsScreenViewModel,
    horizontalPadding: Dp = 0.dp,
    dailyMeasurements: DailyMeasurements,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .navigationBarsPadding(),
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
                meal = dailyMeasurements.getMealByType(
                    type = type
                )
            )
        }
        item {
            BasalInsulinCard(
                viewModel = viewModel,
                dailyMeasurements = dailyMeasurements
            )
        }
    }
}

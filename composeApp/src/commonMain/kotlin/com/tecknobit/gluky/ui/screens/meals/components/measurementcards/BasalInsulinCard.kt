package com.tecknobit.gluky.ui.screens.meals.components.measurementcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.screens.meals.components.FillItemButton
import com.tecknobit.gluky.ui.screens.meals.components.UnfilledMeasurement
import com.tecknobit.gluky.ui.screens.meals.data.BasalInsulin
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.complete_meal

@Composable
fun BasalInsulinCard(
    viewModel: MealsScreenViewModel,
    mealDay: MealDayData,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = GlukyCardColors
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            CardHeader(
                viewModel = viewModel,
                basalInsulin = mealDay.basalInsulin
            )
            UnfilledMeasurement(
                type = BASAL_INSULIN
            )
        }
    }
}

@Composable
private fun CardHeader(
    viewModel: MealsScreenViewModel,
    basalInsulin: BasalInsulin,
) {
    CardHeaderContent(
        item = basalInsulin,
        type = BASAL_INSULIN,
        endContent = {
            FillItemButton(
                contentDescription = string.complete_meal,
                fillDialog = { fill ->

                }
            )
        }
    )
}
package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.icons.BasalInsulin
import com.tecknobit.gluky.ui.screens.meals.data.BasalInsulin
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.basal_insulin
import gluky.composeapp.generated.resources.complete_meal
import org.jetbrains.compose.resources.stringResource

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
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val cardTitle = stringResource(string.basal_insulin)
        Icon(
            imageVector = BasalInsulin,
            contentDescription = cardTitle,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = cardTitle,
            style = AppTypography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            FillItemButton(
                contentDescription = string.complete_meal,
                fillDialog = { fill ->

                }
            )
        }
    }
}
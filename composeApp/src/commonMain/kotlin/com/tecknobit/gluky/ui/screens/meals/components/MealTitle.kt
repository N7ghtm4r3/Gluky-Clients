package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.icons.Apple
import com.tecknobit.gluky.ui.icons.Snack
import com.tecknobit.gluky.ui.icons.Syringe
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.afternoon_snack
import gluky.composeapp.generated.resources.basal_insulin
import gluky.composeapp.generated.resources.breakfast
import gluky.composeapp.generated.resources.dinner
import gluky.composeapp.generated.resources.lunch
import gluky.composeapp.generated.resources.morning_snack
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealTitle(
    modifier: Modifier = Modifier,
    meal: Meal,
    endContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val type = meal.type
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val mealName = stringResource(type.text())
        Icon(
            imageVector = type.icon(),
            contentDescription = mealName,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = mealName,
            style = AppTypography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            endContent?.invoke(this)
        }
    }
}

private fun MeasurementType.icon(): ImageVector {
    return when (this) {
        BREAKFAST -> Icons.Default.BreakfastDining
        MORNING_SNACK -> Snack
        LUNCH -> Icons.Default.LunchDining
        AFTERNOON_SNACK -> Apple
        DINNER -> Icons.Default.DinnerDining
        BASAL_INSULIN -> Syringe
    }
}

private fun MeasurementType.text(): StringResource {
    return when (this) {
        BREAKFAST -> Res.string.breakfast
        MORNING_SNACK -> Res.string.morning_snack
        LUNCH -> Res.string.lunch
        AFTERNOON_SNACK -> Res.string.afternoon_snack
        DINNER -> Res.string.dinner
        BASAL_INSULIN -> Res.string.basal_insulin
    }
}
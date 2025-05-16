@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeRow
import com.tecknobit.equinoxcompose.components.BadgeText
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.ui.icons.Apple
import com.tecknobit.gluky.ui.icons.Snack
import com.tecknobit.gluky.ui.icons.Syringe
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.data.Meal.Companion.levelColor
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
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
import gluky.composeapp.generated.resources.noted_at
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealCard(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
        ) {
            CardHeader(
                meal = meal
            )
            CardContent(
                meal = meal
            )
        }
    }
}

@Composable
private fun CardHeader(
    meal: Meal,
) {
    val type = meal.type
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
    }
    Text(
        modifier = Modifier
            .padding(
                bottom = 5.dp
            ),
        text = stringResource(
            resource = Res.string.noted_at,
            meal.annotationDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        ),
        style = AppTypography.bodyLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
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

@Composable
private fun CardContent(
    meal: Meal,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GlycemiaStatus(
            meal = meal
        )
    }
}

@Composable
private fun GlycemiaStatus(
    meal: Meal,
) {
    val density = LocalDensity.current
    JetLimeRow(
        modifier = Modifier
            .fillMaxWidth(),
        itemsList = ItemsList(meal.glycemiaTrend),
        style = JetLimeDefaults.rowStyle(
            lineBrush = Brush.linearGradient(
                colors = listOf(
                    meal.glycemia.levelColor(),
                    meal.postPrandialGlycemia.levelColor()
                )
            ),
            itemSpacing = with(density) {
                meal.glycemiaGap.toDp()
            },
            contentDistance = 10.dp
        )
    ) { _, glycemia, position ->
        GlycemiaLevel(
            glycemia = glycemia,
            position = position
        )
    }
}

@Composable
private fun GlycemiaLevel(
    glycemia: Int,
    position: EventPosition,
) {
    if (glycemia != -1) {
        val levelColor = glycemia.levelColor()
        JetLimeEvent(
            style = JetLimeEventDefaults.eventStyle(
                position = position,
                pointRadius = 10.dp,
                pointAnimation = null,
                pointStrokeColor = levelColor,
                pointFillColor = levelColor
            )
        ) {
            BadgeText(
                badgeText = glycemia.toString(),
                badgeColor = levelColor,
                textColor = getContrastColor(
                    backgroundColor = levelColor
                )
            )
        }
    }
}
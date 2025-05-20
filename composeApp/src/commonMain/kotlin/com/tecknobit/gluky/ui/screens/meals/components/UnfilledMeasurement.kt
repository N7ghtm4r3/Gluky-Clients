package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import gluky.composeapp.generated.resources.Res.drawable
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.unfilled_afternoon_snack
import gluky.composeapp.generated.resources.unfilled_afternoon_snack_dark
import gluky.composeapp.generated.resources.unfilled_afternoon_snack_light
import gluky.composeapp.generated.resources.unfilled_basal_insulin
import gluky.composeapp.generated.resources.unfilled_basal_insulin_dark
import gluky.composeapp.generated.resources.unfilled_basal_insulin_light
import gluky.composeapp.generated.resources.unfilled_breakfast
import gluky.composeapp.generated.resources.unfilled_breakfast_dark
import gluky.composeapp.generated.resources.unfilled_breakfast_light
import gluky.composeapp.generated.resources.unfilled_dinner
import gluky.composeapp.generated.resources.unfilled_dinner_dark
import gluky.composeapp.generated.resources.unfilled_dinner_light
import gluky.composeapp.generated.resources.unfilled_lunch
import gluky.composeapp.generated.resources.unfilled_lunch_dark
import gluky.composeapp.generated.resources.unfilled_lunch_light
import gluky.composeapp.generated.resources.unfilled_morning_snack
import gluky.composeapp.generated.resources.unfilled_morning_snack_dark
import gluky.composeapp.generated.resources.unfilled_morning_snack_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnfilledMeasurement(
    type: MeasurementType,
) {
    val unfilledEmptyStateRes = remember { type.unfilledEmptyStateRes() }
    val title = stringResource(unfilledEmptyStateRes.first)
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        lightResource = unfilledEmptyStateRes.second,
        darkResource = unfilledEmptyStateRes.third,
        useDarkResource = applyDarkTheme(),
        contentDescription = title,
        title = title,
        titleStyle = AppTypography.bodyLarge,
        resourceSize = 150.dp
    )
}

private fun MeasurementType.unfilledEmptyStateRes(): Triple<StringResource, DrawableResource, DrawableResource> {
    return when (this) {
        BREAKFAST -> {
            Triple(
                first = string.unfilled_breakfast,
                second = drawable.unfilled_breakfast_light,
                third = drawable.unfilled_breakfast_dark
            )
        }

        MORNING_SNACK -> {
            Triple(
                first = string.unfilled_morning_snack,
                second = drawable.unfilled_morning_snack_light,
                third = drawable.unfilled_morning_snack_dark
            )
        }

        LUNCH -> {
            Triple(
                first = string.unfilled_lunch,
                second = drawable.unfilled_lunch_light,
                third = drawable.unfilled_lunch_dark
            )
        }

        AFTERNOON_SNACK -> {
            Triple(
                first = string.unfilled_afternoon_snack,
                second = drawable.unfilled_afternoon_snack_light,
                third = drawable.unfilled_afternoon_snack_dark
            )
        }

        DINNER -> {
            Triple(
                first = string.unfilled_dinner,
                second = drawable.unfilled_dinner_light,
                third = drawable.unfilled_dinner_dark
            )
        }

        BASAL_INSULIN -> {
            Triple(
                first = string.unfilled_basal_insulin,
                second = drawable.unfilled_basal_insulin_light,
                third = drawable.unfilled_basal_insulin_dark
            )
        }
    }
}
package com.tecknobit.gluky.ui.screens.measurements.components.measurementcards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.ui.components.MeasurementTitle
import com.tecknobit.gluky.ui.screens.measurements.data.GlukyItem
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.Res.drawable
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.administered
import gluky.composeapp.generated.resources.afternoon_snack_noted_at
import gluky.composeapp.generated.resources.basal_insulin_noted_at
import gluky.composeapp.generated.resources.breakfast_noted_at
import gluky.composeapp.generated.resources.dinner_noted_at
import gluky.composeapp.generated.resources.insulin_units
import gluky.composeapp.generated.resources.lunch_noted_at
import gluky.composeapp.generated.resources.morning_snack_noted_at
import gluky.composeapp.generated.resources.no_insulin_needed
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
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CardHeaderContent(
    item: GlukyItem,
    type: MeasurementType,
    endContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    MeasurementTitle(
        type = type,
        endContent = endContent
    )
    AnimatedVisibility(
        visible = item.annotationDate.value != -1L
    ) {
        Text(
            text = stringResource(
                resource = type.notedString(),
                item.annotationDate.value.toDateString(
                    pattern = H24_HOURS_MINUTES_PATTERN
                )
            ),
            style = AppTypography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Returner
private fun MeasurementType.notedString(): StringResource {
    return when (this) {
        BREAKFAST -> string.breakfast_noted_at
        MORNING_SNACK -> string.morning_snack_noted_at
        LUNCH -> string.lunch_noted_at
        AFTERNOON_SNACK -> string.afternoon_snack_noted_at
        DINNER -> string.dinner_noted_at
        BASAL_INSULIN -> string.basal_insulin_noted_at
    }
}

@Composable
internal fun CardContentImpl(
    item: GlukyItem,
    type: MeasurementType,
    filledContent: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = item.isNotFilledYet
    ) { isNotFilledYet ->
        if (isNotFilledYet) {
            UnfilledMeasurement(
                type = type
            )
        } else
            filledContent()
    }
}

@Composable
private fun UnfilledMeasurement(
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

@Returner
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

@Composable
internal fun AdministeredInsulinUnits(
    insulinUnits: Int,
) {
    val insulinUnitsText = formatInsulinUnits(
        insulinUnits = insulinUnits
    )
    Text(
        modifier = Modifier
            .heightIn(
                min = 35.dp
            )
            .padding(
                top = 10.dp
            ),
        text = insulinUnitsText,
        style = AppTypography.bodyLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun formatInsulinUnits(
    insulinUnits: Int,
): AnnotatedString {
    if (insulinUnits == -1)
        return AnnotatedString(stringResource(string.no_insulin_needed))
    val primaryColor = MaterialTheme.colorScheme.primary
    val administered = pluralStringResource(
        resource = Res.plurals.administered,
        insulinUnits
    )
    val insulinUnitsText = pluralStringResource(
        resource = Res.plurals.insulin_units,
        insulinUnits
    )
    return remember(insulinUnits) {
        buildAnnotatedString {
            append(administered)
            append(" ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor
                )
            ) {
                append(insulinUnits.toString())
            }
            append(" ")
            append(insulinUnitsText)
        }
    }
}
package com.tecknobit.gluky.ui.screens.meals.components.measurementcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.ui.screens.meals.components.MeasurementTitle
import com.tecknobit.gluky.ui.screens.meals.data.GlukyItem
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.glukycore.enums.MeasurementType
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.noted_at
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
            modifier = Modifier
                .padding(
                    bottom = 5.dp
                ),
            text = stringResource(
                resource = string.noted_at,
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
package com.tecknobit.gluky.ui.screens.meals.components.daypickers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.helpers.asDayOfWeek
import com.tecknobit.gluky.ui.theme.AppTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun DayIndicator(
    modifier: Modifier = Modifier,
    dayInMillis: Long,
    contentColor: Color = Color.White,
    monthVisible: Boolean = true,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                modifier = Modifier
                    .alignBy(
                        alignmentLine = LastBaseline
                    ),
                text = dayInMillis.toDateString(
                    pattern = "dd"
                ),
                style = AppTypography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            if (monthVisible) {
                Text(
                    modifier = Modifier
                        .alignBy(
                            alignmentLine = LastBaseline
                        ),
                    text = "/${
                        dayInMillis.toDateString(
                            pattern = "MM"
                        )
                    }",
                    style = AppTypography.labelLarge,
                    color = contentColor
                )
            }
        }
        Text(
            text = stringResource(dayInMillis.asDayOfWeek()),
            style = AppTypography.titleLarge,
            color = contentColor
        )
    }
}
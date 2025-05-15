package com.tecknobit.gluky.ui.screens.meals.components

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
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.equinoxcore.time.TimeFormatter.toLocalDate
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.fri
import gluky.composeapp.generated.resources.mon
import gluky.composeapp.generated.resources.sat
import gluky.composeapp.generated.resources.sun
import gluky.composeapp.generated.resources.thu
import gluky.composeapp.generated.resources.tue
import gluky.composeapp.generated.resources.wed
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.StringResource
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

@FutureEquinoxApi(
    protoBehavior = """
        Actually is implemented to return just in for languages but will be extended to all the 
        languages supported by Equinox
    """,
    releaseVersion = "1.1.3",
    additionalNotes = """
        - Warn about is temporary method until the official kotlinx's one will be released
        - Check whether the best name is this or something like toDayOfWeek or anyone else
        - Check if overload this method with the @Composable one to return directly the currentDay as String
        - To allow to return uppercase and lowercase 
    """
)
private fun Long.asDayOfWeek(): StringResource {
    val dayOfWeek = this.toLocalDate().dayOfWeek
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> Res.string.mon
        DayOfWeek.TUESDAY -> Res.string.tue
        DayOfWeek.WEDNESDAY -> Res.string.wed
        DayOfWeek.THURSDAY -> Res.string.thu
        DayOfWeek.FRIDAY -> Res.string.fri
        DayOfWeek.SATURDAY -> Res.string.sat
        else -> Res.string.sun
    }
}
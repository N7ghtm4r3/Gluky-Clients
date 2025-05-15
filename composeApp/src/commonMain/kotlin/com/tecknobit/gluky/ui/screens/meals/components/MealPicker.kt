package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.equinoxcore.time.TimeFormatter.toLocalDate
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
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
@LayoutCoordinator
fun MealPicker(
    viewModel: MealsScreenViewModel,
) {
    val day by viewModel.day.collectAsState()
    ResponsiveContent(
        onExpandedSizeClass = {
            PartialDayPicker(
                viewModel = viewModel,
                day = day
            )
        },
        onMediumSizeClass = {
        },
        onMediumWidthExpandedHeight = {
            PartialDayPicker(
                viewModel = viewModel,
                day = day
            )
        },
        onCompactSizeClass = {
            PartialDayPicker(
                viewModel = viewModel,
                day = day
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ResponsiveClassComponent(
    classes = [MEDIUM_EXPANDED_CONTENT, COMPACT_CONTENT]
)
private fun PartialDayPicker(
    viewModel: MealsScreenViewModel,
    day: Long,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val state = rememberPagerState(
            initialPage = 50,
            pageCount = { 100 }
        )
        HorizontalPager(
            state = state,
        ) { page ->
            LaunchedEffect(page) {
                viewModel.computeDayValue(
                    page = page
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DayIndicator(
                    day = day
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .align(Alignment.BottomCenter),
            shape = BottomSheetDefaults.ExpandedShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            MealDay(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun DayIndicator(
    day: Long,
) {
    Text(
        text = day.toDateString(
            pattern = "dd"
        ),
        style = AppTypography.displayLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Text(
        text = stringResource(day.asDayOfWeek()),
        style = AppTypography.titleLarge,
        color = Color.White
    )
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
        - Check if overload this method with the @Composable one to return directly the day as String
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
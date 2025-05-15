@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.time.TimeFormatter.toLocalDate
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.INITIAL_SELECTED_DAY
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.MAX_LOADABLE_DAYS
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.ONE_DAY_MILLIS
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.april
import gluky.composeapp.generated.resources.august
import gluky.composeapp.generated.resources.december
import gluky.composeapp.generated.resources.february
import gluky.composeapp.generated.resources.january
import gluky.composeapp.generated.resources.july
import gluky.composeapp.generated.resources.june
import gluky.composeapp.generated.resources.march
import gluky.composeapp.generated.resources.may
import gluky.composeapp.generated.resources.november
import gluky.composeapp.generated.resources.october
import gluky.composeapp.generated.resources.september
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val indicatorWidth = 125.dp

@Composable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_EXPANDED_CONTENT]
)
fun DayPickerBar(
    viewModel: MealsScreenViewModel,
    currentDay: Long,
) {
    // Compute the window to use given the current days
    val daysWindow: MutableList<Long> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        repeat(MAX_LOADABLE_DAYS) { index ->
            val dayValue = computeDayMillis(
                currentDay = currentDay,
                index = index,
            )
            daysWindow.add(dayValue)
        }
    }
    // Scroll to the currentDay position to center it in the PickerBar
    val state = rememberLazyGridState()
    var containerWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    LaunchedEffect(containerWidth) {
        if (containerWidth > 0) {
            val itemWidthPx = with(density) {
                indicatorWidth.toPx()
            }
            val centerOffset = (containerWidth / 2 - itemWidthPx / 2).toInt()
            state.scrollToItem(
                index = INITIAL_SELECTED_DAY,
                scrollOffset = -centerOffset
            )
        }
    }
    /* Retrieve the month of the first item displayed by the PickerBar or the month of the
     selected day */
    val currentMonth = remember { mutableStateOf(currentDay.asMonth()) }
    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                currentMonth.value = daysWindow[it].asMonth()
            }
    }
    Column(
        modifier = Modifier
            .height(135.dp)
            .responsiveMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    size = 16.dp
                )
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 10.dp
                ),
            text = stringResource(currentMonth.value),
            style = AppTypography.titleMedium,
            color = Color.White
        )
        PickerBar(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    containerWidth = coordinates.size.width
                },
            viewModel = viewModel,
            daysWindow = daysWindow,
            currentMonth = currentMonth,
            currentDay = currentDay,
            state = state
        )
    }
}

private fun computeDayMillis(
    currentDay: Long,
    index: Int,
): Long {
    val offset = index - INITIAL_SELECTED_DAY
    return (currentDay + (offset * ONE_DAY_MILLIS))
}

@Composable
private fun PickerBar(
    modifier: Modifier = Modifier,
    viewModel: MealsScreenViewModel,
    daysWindow: List<Long>,
    currentMonth: MutableState<StringResource>,
    currentDay: Long,
    state: LazyGridState,
) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = state
    ) {
        items(
            items = daysWindow,
            key = { day -> day }
        ) { day ->
            DayIndicator(
                modifier = Modifier
                    .width(indicatorWidth)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp
                        )
                    )
                    .clickable {
                        viewModel.setCurrentDay(
                            millis = day
                        )
                        currentMonth.value = day.asMonth()
                    },
                dayInMillis = day,
                contentColor = if (day == currentDay)
                    MaterialTheme.colorScheme.primary
                else
                    Color.White,
                monthVisible = false
            )
        }
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
        - Check whether the best name is this or something like asMonth or anyone else
        - Check if overload this method with the @Composable one to return directly the currentDay as String
        - To allow to return uppercase and lowercase 
    """
)
private fun Long.asMonth(): StringResource {
    val month = this.toLocalDate().month
    return when (month) {
        Month.JANUARY -> Res.string.january
        Month.FEBRUARY -> Res.string.february
        Month.MARCH -> Res.string.march
        Month.APRIL -> Res.string.april
        Month.MAY -> Res.string.may
        Month.JUNE -> Res.string.june
        Month.JULY -> Res.string.july
        Month.AUGUST -> Res.string.august
        Month.SEPTEMBER -> Res.string.september
        Month.OCTOBER -> Res.string.october
        Month.NOVEMBER -> Res.string.november
        else -> Res.string.december
    }
}
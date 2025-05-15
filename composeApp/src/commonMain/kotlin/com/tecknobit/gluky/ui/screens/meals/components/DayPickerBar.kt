@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.INITIAL_SELECTED_DAY
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.MAX_LOADABLE_DAYS
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel.Companion.ONE_DAY_MILLIS

@Composable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_EXPANDED_CONTENT]
)
fun DayPickerBar(
    viewModel: MealsScreenViewModel,
    currentDay: Long,
) {
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
    Column(
        modifier = Modifier
            .height(125.dp)
            .responsiveMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    size = 16.dp
                )
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PickerBar(
            viewModel = viewModel,
            daysWindow = daysWindow,
            currentDay = currentDay
        )
    }
}

private fun computeDayMillis(
    currentDay: Long,
    index: Int,
): Long {
    val offset = index - INITIAL_SELECTED_DAY
    return currentDay + (offset * ONE_DAY_MILLIS)
}

@Composable
private fun PickerBar(
    viewModel: MealsScreenViewModel,
    daysWindow: List<Long>,
    currentDay: Long,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = rememberLazyGridState(
            initialFirstVisibleItemIndex = INITIAL_SELECTED_DAY
        )
    ) {
        items(
            items = daysWindow,
            key = { day -> day }
        ) { day ->
            DayIndicator(
                modifier = Modifier
                    .width(125.dp)
                    .clickable {
                        viewModel.setCurrentDay(
                            millis = day
                        )
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
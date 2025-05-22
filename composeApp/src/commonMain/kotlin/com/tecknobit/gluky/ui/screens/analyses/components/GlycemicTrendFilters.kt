package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod

@Composable
fun GroupingDayChip(
    viewModel: AnalysesScreenViewModel,
    groupingDay: GlycemicTrendGroupingDay,
) {
    FilterChip(
        selected = false,
        onClick = {

        },
        label = {
            Text(
                text = "Days"
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun TrendPeriodChip(
    viewModel: AnalysesScreenViewModel,
    trendPeriod: GlycemicTrendPeriod,
) {
    FilterChip(
        selected = true,
        onClick = {

        },
        label = {
            Text(
                text = "Period"
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun CustomPeriodButton(
    viewModel: AnalysesScreenViewModel,
) {
    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = ""
        )
    }
}
package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.FRIDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.MONDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.SATURDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.SUNDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.THURSDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.TUESDAY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.WEDNESDAY
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.FOUR_MONTHS
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.THREE_MONTHS
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.all
import gluky.composeapp.generated.resources.four_months_extended
import gluky.composeapp.generated.resources.friday
import gluky.composeapp.generated.resources.monday
import gluky.composeapp.generated.resources.one_month_extended
import gluky.composeapp.generated.resources.one_week_extended
import gluky.composeapp.generated.resources.saturday
import gluky.composeapp.generated.resources.select_grouping_day
import gluky.composeapp.generated.resources.select_range_period
import gluky.composeapp.generated.resources.sunday
import gluky.composeapp.generated.resources.three_months_extended
import gluky.composeapp.generated.resources.thursday
import gluky.composeapp.generated.resources.tuesday
import gluky.composeapp.generated.resources.wednesday
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GroupingDayChip(
    viewModel: AnalysesScreenViewModel,
    groupingDay: GlycemicTrendGroupingDay?,
) {
    Column {
        val select = remember { mutableStateOf(false) }
        FilterChip(
            selected = select.value,
            onClick = { select.value = !select.value },
            label = {
                Text(
                    text = stringResource(groupingDay.text())
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(Res.string.select_grouping_day)
                )
            }
        )
        GroupingDayDropdownMenu(
            select = select,
            viewModel = viewModel
        )
    }
}

@Composable
private fun GroupingDayDropdownMenu(
    select: MutableState<Boolean>,
    viewModel: AnalysesScreenViewModel,
) {
    FiltersDropdownMenu(
        select = select
    ) {
        GlycemicTrendGroupingDay.entries.forEach { groupingDay ->
            DropdownMenuItem(
                onClick = {
                    viewModel.selectGlycemicGroupingDay(
                        groupingDay = groupingDay
                    )
                    select.value = false
                },
                text = {
                    Text(
                        text = stringResource(groupingDay.text())
                    )
                }
            )
        }
    }
}

@Returner
private fun GlycemicTrendGroupingDay?.text(): StringResource {
    return when (this) {
        SUNDAY -> Res.string.sunday
        MONDAY -> Res.string.monday
        TUESDAY -> Res.string.tuesday
        WEDNESDAY -> Res.string.wednesday
        THURSDAY -> Res.string.thursday
        FRIDAY -> Res.string.friday
        SATURDAY -> Res.string.saturday
        else -> Res.string.all
    }
}

@Composable
fun TrendPeriodChip(
    viewModel: AnalysesScreenViewModel,
    trendPeriod: GlycemicTrendPeriod,
) {
    Column {
        val select = remember { mutableStateOf(false) }
        FilterChip(
            selected = select.value,
            onClick = { select.value = !select.value },
            label = {
                Text(
                    text = stringResource(trendPeriod.text())
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(Res.string.select_range_period)
                )
            }
        )
        TrendPeriodDropdownMenu(
            select = select,
            viewModel = viewModel
        )
    }
}

@Composable
private fun TrendPeriodDropdownMenu(
    select: MutableState<Boolean>,
    viewModel: AnalysesScreenViewModel,
) {
    FiltersDropdownMenu(
        select = select
    ) {
        GlycemicTrendPeriod.entries.forEach { period ->
            DropdownMenuItem(
                onClick = {
                    viewModel.selectGlycemicTrendPeriod(
                        period = period
                    )
                    select.value = false
                },
                text = {
                    Text(
                        text = stringResource(period.text())
                    )
                }
            )
        }
    }
}

@Returner
private fun GlycemicTrendPeriod.text(): StringResource {
    return when (this) {
        ONE_WEEK -> Res.string.one_week_extended
        ONE_MONTH -> Res.string.one_month_extended
        THREE_MONTHS -> Res.string.three_months_extended
        FOUR_MONTHS -> Res.string.four_months_extended
    }
}

@Composable
private fun FiltersDropdownMenu(
    select: MutableState<Boolean>,
    itemContent: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = select.value,
        onDismissRequest = { select.value = !select.value },
        shape = RoundedCornerShape(
            size = 12.dp
        ),
        content = itemContent
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
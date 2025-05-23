@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.theme.DialogShape
import com.tecknobit.gluky.ui.theme.useDialogSize
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@LayoutCoordinator
fun CustomPeriodPicker(
    show: MutableState<Boolean>,
    viewModel: AnalysesScreenViewModel,
    glycemicTrendData: GlycemicTrendData,
    trendPeriod: GlycemicTrendPeriod,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            PeriodPickerDialog(
                show = show,
                glycemicTrendData = glycemicTrendData
            )
        },
        onMediumSizeClass = {
            PeriodPickerDialog(
                show = show,
                glycemicTrendData = glycemicTrendData
            )
        },
        onCompactSizeClass = {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scope = rememberCoroutineScope()
            if (show.value) {
                scope.launch {
                    sheetState.show()
                }
            }
            PeriodPickerBottomSheet(
                show = show,
                state = sheetState,
                scope = scope,
                glycemicTrendData = glycemicTrendData
            )
        }
    )
}

@Composable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun PeriodPickerDialog(
    show: MutableState<Boolean>,
    glycemicTrendData: GlycemicTrendData,
) {
    EquinoxDialog(
        show = show
    ) {
        PeriodPickerContent(
            modifier = Modifier
                .clip(DialogShape),
            glycemicTrendData = glycemicTrendData
        )
    }
}

@Composable
@CompactClassComponent
private fun PeriodPickerBottomSheet(
    show: MutableState<Boolean>,
    state: SheetState,
    scope: CoroutineScope,
    glycemicTrendData: GlycemicTrendData,
) {
    if (state.isVisible) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = {
                scope.launch {
                    show.value = false
                    state.hide()
                }
            }
        ) {
            PeriodPickerContent(
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent
                ),
                glycemicTrendData = glycemicTrendData
            )
        }
    }
}

@Composable
private fun PeriodPickerContent(
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    glycemicTrendData: GlycemicTrendData,
) {
    val rangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = glycemicTrendData.firstAvailableDate(),
        initialSelectedEndDateMillis = glycemicTrendData.lastAvailableDate(),
        selectableDates = DatesValidator
    )
    DateRangePicker(
        modifier = modifier
            .useDialogSize(),
        colors = colors,
        state = rangePickerState,
        showModeToggle = false
    )
}

private object DatesValidator : SelectableDates {

    private val currentTimestamp = TimeFormatter.currentTimestamp()

    override fun isSelectableDate(
        utcTimeMillis: Long,
    ): Boolean {
        return utcTimeMillis <= currentTimestamp
    }

}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.gluky.helpers.extendedText
import com.tecknobit.gluky.ui.components.SaveButton
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.DialogShape
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.gluky.ui.theme.useDialogSize
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.select_range
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
@LayoutCoordinator
fun CustomPeriodPicker(
    show: MutableState<Boolean>,
    viewModel: AnalysesScreenViewModel,
    glycemicTrendData: GlycemicTrendDataContainer,
    trendPeriod: GlycemicTrendPeriod,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            PeriodPickerDialog(
                show = show,
                viewModel = viewModel,
                glycemicTrendData = glycemicTrendData,
                trendPeriod = trendPeriod
            )
        },
        onMediumSizeClass = {
            PeriodPickerDialog(
                show = show,
                viewModel = viewModel,
                glycemicTrendData = glycemicTrendData,
                trendPeriod = trendPeriod
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
                viewModel = viewModel,
                glycemicTrendData = glycemicTrendData,
                trendPeriod = trendPeriod
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
    viewModel: AnalysesScreenViewModel,
    glycemicTrendData: GlycemicTrendDataContainer,
    trendPeriod: GlycemicTrendPeriod,
) {
    EquinoxDialog(
        show = show
    ) {
        ToasterContainer(
            viewModel = viewModel
        )
        PeriodPickerContent(
            modifier = Modifier
                .clip(DialogShape),
            viewModel = viewModel,
            glycemicTrendData = glycemicTrendData,
            trendPeriod = trendPeriod,
            onSave = { show.value = false }
        )
    }
}

@Composable
@CompactClassComponent
private fun PeriodPickerBottomSheet(
    show: MutableState<Boolean>,
    state: SheetState,
    scope: CoroutineScope,
    viewModel: AnalysesScreenViewModel,
    glycemicTrendData: GlycemicTrendDataContainer,
    trendPeriod: GlycemicTrendPeriod,
) {
    if (state.isVisible) {
        val closeSheet = {
            scope.launch {
                show.value = false
                state.hide()
            }
        }
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = { closeSheet() }
        ) {
            ToasterContainer(
                viewModel = viewModel
            )
            PeriodPickerContent(
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent
                ),
                viewModel = viewModel,
                glycemicTrendData = glycemicTrendData,
                trendPeriod = trendPeriod,
                onSave = { closeSheet() }
            )
        }
    }
}

@Composable
private fun ToasterContainer(
    viewModel: AnalysesScreenViewModel,
) {
    viewModel.toasterState = rememberToasterState()
    Toaster(
        state = viewModel.toasterState,
        darkTheme = applyDarkTheme(),
        richColors = true
    )
}

@Composable
private fun PeriodPickerContent(
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    viewModel: AnalysesScreenViewModel,
    glycemicTrendData: GlycemicTrendDataContainer,
    trendPeriod: GlycemicTrendPeriod,
    onSave: () -> Unit,
) {
    val rangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = glycemicTrendData.firstAvailableDate(),
        initialSelectedEndDateMillis = glycemicTrendData.lastAvailableDate(),
        selectableDates = DatesValidator
    )
    val trendPeriodString = stringResource(trendPeriod.extendedText())
    DateRangePicker(
        modifier = modifier
            .useDialogSize(),
        colors = colors,
        state = rangePickerState,
        showModeToggle = false,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.select_range),
                    style = AppTypography.titleLarge
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    SaveButton(
                        modifier = Modifier
                            .align(Alignment.End),
                        save = {
                            viewModel.applyCustomTrendPeriod(
                                state = rangePickerState,
                                allowedPeriod = trendPeriodString,
                                onApply = onSave
                            )
                        }
                    )
                }
            }
        },
        headline = null
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
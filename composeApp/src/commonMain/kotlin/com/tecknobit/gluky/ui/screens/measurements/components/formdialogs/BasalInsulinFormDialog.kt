package com.tecknobit.gluky.ui.screens.measurements.components.formdialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.gluky.ui.screens.measurements.data.types.BasalInsulin
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.blood_sugar
import gluky.composeapp.generated.resources.glycemic_value

/**
 * Custom [EquinoxDialog] use to fill the details of the basal insulin measurement
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel of the screen
 * @param basalInsulin The basal insulin element to fill
 */
@Composable
fun BasalInsulinFormDialog(
    show: MutableState<Boolean>,
    viewModel: MeasurementsScreenViewModel,
    basalInsulin: BasalInsulin,
) {
    MeasurementFormDialogContainer(
        show = show,
        viewModel = viewModel,
        type = BASAL_INSULIN,
        content = {
            GlycemiaSection(
                viewModel = viewModel,
                basalInsulin = basalInsulin
            )
            InsulinSection(
                viewModel = viewModel,
                item = basalInsulin
            )
        },
        save = {
            viewModel.fillBasalInsulin(
                basalInsulin = basalInsulin,
                onSave = { show.value = false }
            )
        }
    )
}

/**
 * Section of the form where the user can insert the relative glycemia value
 *
 * @param viewModel The support viewmodel of the screen
 * @param basalInsulin The basal insulin element to fill
 */
@Composable
private fun GlycemiaSection(
    viewModel: MeasurementsScreenViewModel,
    basalInsulin: BasalInsulin,
) {
    FormSection(
        sectionTitle = Res.string.blood_sugar
    ) {
        viewModel.glycemia = remember {
            basalInsulin.glycemia.toInitialGlycemicValue()
        }
        viewModel.glycemiaError = remember { mutableStateOf(false) }
        GlycemiaInputField(
            title = Res.string.glycemic_value,
            glycemia = viewModel.glycemia,
            glycemiaError = viewModel.glycemiaError,
            imeAction = ImeAction.Next
        )
    }
}
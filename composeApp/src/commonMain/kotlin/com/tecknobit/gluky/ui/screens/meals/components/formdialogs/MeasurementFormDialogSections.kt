package com.tecknobit.gluky.ui.screens.meals.components.formdialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.quantitypicker.QuantityPicker
import com.tecknobit.equinoxcompose.components.quantitypicker.rememberQuantityPickerState
import com.tecknobit.gluky.ui.components.SaveButton
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.meals.components.MeasurementTitle
import com.tecknobit.gluky.ui.screens.meals.data.GlukyItem
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.DialogShape
import com.tecknobit.gluky.ui.theme.InputFieldHeight
import com.tecknobit.gluky.ui.theme.InputFieldShape
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.gluky.ui.theme.useDialogSize
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.glycemiaValueIsValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.blood_sugar_placeholder
import gluky.composeapp.generated.resources.close
import gluky.composeapp.generated.resources.insulin
import gluky.composeapp.generated.resources.no_insulin_needed
import gluky.composeapp.generated.resources.units
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MeasurementFormDialogContainer(
    show: MutableState<Boolean>,
    viewModel: MealsScreenViewModel,
    type: MeasurementType,
    content: @Composable ColumnScope.() -> Unit,
    save: () -> Unit,
) {
    EquinoxDialog(
        show = show,
        viewModel = viewModel,
    ) {
        viewModel.toasterState = rememberToasterState()
        Toaster(
            state = viewModel.toasterState,
            darkTheme = applyDarkTheme(),
            richColors = true
        )
        Surface(
            modifier = Modifier
                .useDialogSize(),
            shape = DialogShape,
            color = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    )
            ) {
                FormTitle(
                    show = show,
                    type = type
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = content
                )
                val softwareKeyboardController = LocalSoftwareKeyboardController.current
                SaveButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    save = {
                        softwareKeyboardController?.hide()
                        save()
                    }
                )
            }
        }
    }
}

@Composable
private fun FormTitle(
    show: MutableState<Boolean>,
    type: MeasurementType,
) {
    MeasurementTitle(
        type = type,
        endContent = {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = { show.value = false }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close)
                )
            }
        }
    )
}

internal fun MutableState<Int>.toInitialGlycemicValue(): MutableState<String> {
    return mutableStateOf(
        if (value == -1)
            ""
        else
            value.toString()
    )
}

@Composable
internal fun GlycemiaInputField(
    modifier: Modifier = Modifier,
    title: StringResource,
    glycemia: MutableState<String>,
    glycemiaError: MutableState<Boolean>,
    imeAction: ImeAction,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(title),
            style = AppTypography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(InputFieldHeight),
            value = glycemia,
            placeholder = Res.string.blood_sugar_placeholder,
            shape = InputFieldShape,
            isError = glycemiaError,
            validator = { glycemiaValueIsValid(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = KeyboardType.NumberPassword
            )
        )
    }
}

@OptIn(ExperimentalComposeApi::class)
@Composable
internal fun InsulinSection(
    viewModel: MealsScreenViewModel,
    item: GlukyItem,
) {
    val mealNotFilledYet = item.isNotFilledYet
    val insulinUnits = item.insulinUnits.value
    viewModel.insulinUnits = rememberQuantityPickerState(
        initialQuantity = if (mealNotFilledYet || insulinUnits == -1)
            1
        else
            insulinUnits,
        minQuantity = 1,
        longPressQuantity = 2
    )
    viewModel.insulinNeeded = remember {
        mutableStateOf(
            if (mealNotFilledYet)
                true
            else
                insulinUnits != -1
        )
    }
    FormSection(
        sectionTitle = Res.string.insulin,
        verticalSpacing = 5.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Checkbox(
                    checked = !viewModel.insulinNeeded.value,
                    onCheckedChange = {
                        viewModel.insulinNeeded.value = !viewModel.insulinNeeded.value
                    }
                )
            }
            Text(
                text = stringResource(Res.string.no_insulin_needed),
                style = AppTypography.labelLarge
            )
        }
        // TODO: WHEN QuantityPicker HAS INTEGRATED THE ENABLED PROPERTY REMOVE AND USE DIRECTLY THAT
        AnimatedVisibility(
            visible = viewModel.insulinNeeded.value
        ) {
            QuantityPicker(
                state = viewModel.insulinUnits,
                informativeText = stringResource(Res.string.units)
            )
        }
    }
}

@Composable
internal fun FormSection(
    sectionTitle: StringResource,
    verticalSpacing: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        SectionTitle(
            title = sectionTitle
        )
        content()
    }
}
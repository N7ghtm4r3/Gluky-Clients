@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.quantitypicker.QuantityPicker
import com.tecknobit.equinoxcompose.components.quantitypicker.rememberQuantityPickerState
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.InputFieldHeight
import com.tecknobit.gluky.ui.theme.InputFieldShape
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.blood_sugar
import gluky.composeapp.generated.resources.blood_sugar_placeholder
import gluky.composeapp.generated.resources.close
import gluky.composeapp.generated.resources.insulin
import gluky.composeapp.generated.resources.no_insulin_needed
import gluky.composeapp.generated.resources.post_prandial
import gluky.composeapp.generated.resources.pre_prandial
import gluky.composeapp.generated.resources.save
import gluky.composeapp.generated.resources.units
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealFormDialog(
    show: MutableState<Boolean>,
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    EquinoxDialog(
        show = show,
        viewModel = viewModel,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    max = 750.dp
                ),
            shape = RoundedCornerShape(
                size = 16.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    )
            ) {
                FormTitle(
                    show = show,
                    meal = meal
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlycemiaSection(
                        viewModel = viewModel,
                        meal = meal
                    )
                    InsulinSection(
                        viewModel = viewModel,
                        meal = meal
                    )
                    Button(
                        modifier = Modifier
                            .align(Alignment.End)
                            .width(100.dp),
                        onClick = {
                            viewModel.fillMeal(
                                meal = meal,
                                onSave = { show.value = false }
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.save),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormTitle(
    show: MutableState<Boolean>,
    meal: Meal,
) {
    MealTitle(
        meal = meal,
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

@Composable
private fun GlycemiaSection(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    FormSection(
        sectionTitle = Res.string.blood_sugar
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            viewModel.glycemia = remember {
                mutableStateOf(
                    if (meal.isSettled)
                        meal.glycemia.toString()
                    else
                        ""
                )
            }
            viewModel.glycemiaError = remember { mutableStateOf(false) }
            GlycemiaInputField(
                title = Res.string.pre_prandial,
                glycemia = viewModel.glycemia,
                glycemiaError = viewModel.glycemiaError
            )
            viewModel.postPrandialGlycemia = remember {
                mutableStateOf(
                    if (meal.isSettled)
                        meal.postPrandialGlycemia.toString()
                    else
                        ""
                )
            }
            viewModel.postPrandialGlycemiaError = remember { mutableStateOf(false) }
            GlycemiaInputField(
                title = Res.string.post_prandial,
                glycemia = viewModel.postPrandialGlycemia,
                glycemiaError = viewModel.postPrandialGlycemiaError
            )
        }
    }
}

@Composable
private fun RowScope.GlycemiaInputField(
    title: StringResource,
    glycemia: MutableState<String>,
    glycemiaError: MutableState<Boolean>,
) {
    Column(
        modifier = Modifier
            .weight(1f),
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
            isError = glycemiaError
        )
    }
}

@Composable
private fun InsulinSection(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    val initialInsulinUnit = meal.insulinUnits
    viewModel.insulinUnit = rememberQuantityPickerState(
        initialQuantity = if (initialInsulinUnit != -1)
            initialInsulinUnit
        else
            1,
        minQuantity = 1,
        longPressQuantity = 2
    )
    FormSection(
        sectionTitle = Res.string.insulin,
        verticalSpacing = 5.dp
    ) {
        var insulinNeeded by remember { mutableStateOf(true) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Checkbox(
                    checked = !insulinNeeded,
                    onCheckedChange = { insulinNeeded = !insulinNeeded }
                )
            }
            Text(
                text = stringResource(Res.string.no_insulin_needed),
                style = AppTypography.labelLarge
            )
        }
        // TODO: WHEN QuantityPicker HAS INTEGRATED THE ENABLED PROPERTY REMOVE AND USE DIRECTLY THAT
        AnimatedVisibility(
            visible = insulinNeeded
        ) {
            QuantityPicker(
                state = viewModel.insulinUnit,
                informativeText = stringResource(Res.string.units)
            )
        }
    }
}

@Composable
private fun FormSection(
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
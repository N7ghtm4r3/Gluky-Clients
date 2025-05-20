@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.InputFieldHeight
import com.tecknobit.gluky.ui.theme.InputFieldShape
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.glycemiaValueIsValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.add_meal_entry
import gluky.composeapp.generated.resources.blood_sugar
import gluky.composeapp.generated.resources.blood_sugar_placeholder
import gluky.composeapp.generated.resources.close
import gluky.composeapp.generated.resources.insulin
import gluky.composeapp.generated.resources.meal_entry_placeholder
import gluky.composeapp.generated.resources.meal_entry_quantity_placeholder
import gluky.composeapp.generated.resources.no_insulin_needed
import gluky.composeapp.generated.resources.post_prandial
import gluky.composeapp.generated.resources.pre_prandial
import gluky.composeapp.generated.resources.remove_meal_entry
import gluky.composeapp.generated.resources.save
import gluky.composeapp.generated.resources.units
import gluky.composeapp.generated.resources.what_i_ate
import kotlinx.coroutines.launch
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
        viewModel.toaster = rememberToasterState()
        Toaster(
            state = viewModel.toaster,
            darkTheme = applyDarkTheme(),
            richColors = true
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    max = 750.dp
                ),
            shape = RoundedCornerShape(
                size = 16.dp
            ),
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
                    meal = meal
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
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
                    MealContentSection(
                        viewModel = viewModel,
                        meal = meal
                    )
                    val softwareKeyboardController = LocalSoftwareKeyboardController.current
                    Button(
                        modifier = Modifier
                            .align(Alignment.End)
                            .width(100.dp),
                        onClick = {
                            softwareKeyboardController?.hide()
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
    MeasurementTitle(
        type = meal.type,
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
                meal.glycemia.toInitialGlycemicValue()
            }
            viewModel.glycemiaError = remember { mutableStateOf(false) }
            GlycemiaInputField(
                title = Res.string.pre_prandial,
                glycemia = viewModel.glycemia,
                glycemiaError = viewModel.glycemiaError,
                imeAction = ImeAction.Next
            )
            viewModel.postPrandialGlycemia = remember {
                meal.postPrandialGlycemia.toInitialGlycemicValue()
            }
            viewModel.postPrandialGlycemiaError = remember { mutableStateOf(false) }
            GlycemiaInputField(
                title = Res.string.post_prandial,
                glycemia = viewModel.postPrandialGlycemia,
                glycemiaError = viewModel.postPrandialGlycemiaError,
                imeAction = ImeAction.Done
            )
        }
    }
}

private fun MutableState<Int>.toInitialGlycemicValue(): MutableState<String> {
    return mutableStateOf(
        if (value == -1)
            ""
        else
            value.toString()
    )
}

@Composable
private fun RowScope.GlycemiaInputField(
    title: StringResource,
    glycemia: MutableState<String>,
    glycemiaError: MutableState<Boolean>,
    imeAction: ImeAction,
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
            isError = glycemiaError,
            validator = { glycemiaValueIsValid(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = KeyboardType.NumberPassword
            )
        )
    }
}

@Composable
private fun InsulinSection(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    val mealNotFilledYet = meal.isNotFilledYet
    val insulinUnits = meal.insulinUnits.value
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
private fun MealContentSection(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    ConvertRawData(
        viewModel = viewModel,
        meal = meal
    )
    FormSection(
        sectionTitle = Res.string.what_i_ate
    ) {
        val lazyListState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .heightIn(
                    max = 150.dp
                )
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                vertical = 10.dp
            ),
            state = lazyListState
        ) {
            stickyHeader {
                AddMealEntryButton(
                    viewModel = viewModel,
                    lazyListState = lazyListState
                )
            }
            itemsIndexed(
                items = viewModel.mealContent,
                key = { _, mealEntry -> mealEntry.hashCode() }
            ) { index, mealEntry ->
                MealEntry(
                    viewModel = viewModel,
                    mealEntry = mealEntry,
                    index = index
                )
            }
        }
    }
}

@Composable
private fun ConvertRawData(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    viewModel.mealContent = remember { mutableStateListOf() }
    LaunchedEffect(meal.rawContent) {
        meal.rawContent.value.forEach { entry ->
            val mealEntry = Pair(
                first = mutableStateOf(entry.key),
                second = mutableStateOf(entry.value.treatsAsString())
            )
            viewModel.mealContent.add(mealEntry)
        }
    }
}

@Composable
private fun AddMealEntryButton(
    viewModel: MealsScreenViewModel,
    lazyListState: LazyListState,
) {
    val scope = rememberCoroutineScope()
    SmallFloatingActionButton(
        modifier = Modifier
            .size(30.dp),
        onClick = {
            viewModel.mealContent.add(
                element = Pair(
                    first = mutableStateOf(""),
                    second = mutableStateOf("")
                )
            )
            scope.launch {
                lazyListState.animateScrollToItem(
                    index = viewModel.mealContent.size
                )
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(Res.string.add_meal_entry)
        )
    }
}

@Composable
private fun LazyItemScope.MealEntry(
    viewModel: MealsScreenViewModel,
    mealEntry: Pair<MutableState<String>, MutableState<String>>,
    index: Int,
) {
    Row(
        modifier = Modifier
            .animateItem(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .weight(2f)
                .height(InputFieldHeight),
            value = mealEntry.first,
            shape = InputFieldShape,
            placeholder = Res.string.meal_entry_placeholder,
            trailingIcon = null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            validator = { it.isNotEmpty() }
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(InputFieldHeight),
            value = mealEntry.second,
            shape = InputFieldShape,
            placeholder = Res.string.meal_entry_quantity_placeholder,
            trailingIcon = null,
            keyboardOptions = KeyboardOptions(
                imeAction = if (index == viewModel.mealContent.lastIndex)
                    ImeAction.Done
                else
                    ImeAction.Next
            ),
            validator = { it.isNotEmpty() }
        )
        Icon(
            modifier = Modifier
                .semantics(
                    properties = { role = Role.Button }
                )
                .clip(CircleShape)
                .clickable { viewModel.mealContent.remove(mealEntry) },
            imageVector = Icons.Default.Delete,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = stringResource(Res.string.remove_meal_entry)
        )
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
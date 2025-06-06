@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.measurements.components.formdialogs

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.gluky.ui.screens.measurements.data.types.Meal
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.theme.InputFieldHeight
import com.tecknobit.gluky.ui.theme.InputFieldShape
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.add_meal_entry
import gluky.composeapp.generated.resources.blood_sugar
import gluky.composeapp.generated.resources.meal_entry_placeholder
import gluky.composeapp.generated.resources.meal_entry_quantity_placeholder
import gluky.composeapp.generated.resources.post_prandial
import gluky.composeapp.generated.resources.pre_prandial
import gluky.composeapp.generated.resources.remove_meal_entry
import gluky.composeapp.generated.resources.what_i_ate
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [EquinoxDialog] use to fill the details of a meal measurement
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal element to fill
 */
@Composable
fun MealFormDialog(
    show: MutableState<Boolean>,
    viewModel: MeasurementsScreenViewModel,
    meal: Meal,
) {
    MeasurementFormDialogContainer(
        show = show,
        viewModel = viewModel,
        type = meal.type,
        content = {
            GlycemiaSection(
                viewModel = viewModel,
                meal = meal
            )
            InsulinSection(
                viewModel = viewModel,
                item = meal
            )
            MealContentSection(
                viewModel = viewModel,
                meal = meal
            )
        },
        save = {
            viewModel.fillMeal(
                meal = meal,
                onSave = { show.value = false }
            )
        }
    )
}

/**
 * Section of the form where the user can insert the relative glycemia value
 *
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal element to fill
 */
@Composable
private fun GlycemiaSection(
    viewModel: MeasurementsScreenViewModel,
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
                modifier = Modifier
                    .weight(1f),
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
                modifier = Modifier
                    .weight(1f),
                title = Res.string.post_prandial,
                glycemia = viewModel.postPrandialGlycemia,
                glycemiaError = viewModel.postPrandialGlycemiaError,
                imeAction = ImeAction.Done
            )
        }
    }
}

/**
 * Section of the form where the user can insert the content of the meal
 *
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal element to fill
 */
@Composable
private fun MealContentSection(
    viewModel: MeasurementsScreenViewModel,
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

/**
 * Utility method used to convert the raw data of the meal content in a format compatible with
 * the [MeasurementsScreenViewModel.mealContent] instance
 *
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal element to fill
 */
@Assembler
@Composable
private fun ConvertRawData(
    viewModel: MeasurementsScreenViewModel,
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

/**
 * Custom [SmallFloatingActionButton] used to add a meal entry for the [MealContentSection]
 *
 * @param viewModel The support viewmodel of the screen
 * @param lazyListState The state which manage the behavior of the [MealContentSection]
 *
 */
@Composable
private fun AddMealEntryButton(
    viewModel: MeasurementsScreenViewModel,
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

/**
 * The meal entry component allows the user to insert the type of the food or beverage and the
 * relative quantity
 *
 * @param viewModel The support viewmodel of the screen
 * @param mealEntry The container of the meal entry details
 * @param index The index occupied by the entry
 *
 */
@Composable
private fun LazyItemScope.MealEntry(
    viewModel: MeasurementsScreenViewModel,
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
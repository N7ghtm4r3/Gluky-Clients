package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.InputFieldHeight
import com.tecknobit.gluky.ui.theme.InputFieldShape
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.blood_sugar
import gluky.composeapp.generated.resources.blood_sugar_placeholder
import gluky.composeapp.generated.resources.post_prandial
import gluky.composeapp.generated.resources.pre_prandial
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
                    max = 550.dp
                ),
            shape = RoundedCornerShape(
                size = 16.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp
                    )
                    .padding(
                        top = 10.dp
                    )
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
                                contentDescription = ""
                            )
                        }
                    }
                )
                GlycemiaFormSection(
                    meal = meal
                )
            }
        }
    }
}

@Composable
private fun GlycemiaFormSection(
    meal: Meal,
) {
    val glycemia = remember {
        mutableStateOf(
            if (meal.isSettled)
                "${meal.glycemia}"
            else
                ""
        )
    }
    val glycemiaError = remember { mutableStateOf(false) }
    val postPrandialGlycemia = remember {
        mutableStateOf(
            if (meal.isSettled)
                "${meal.postPrandialGlycemia}"
            else
                ""
        )
    }
    val postPrandialGlycemiaError = remember { mutableStateOf(false) }
    Column {
        SectionTitle(
            title = Res.string.blood_sugar
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlycemiaInputField(
                modifier = Modifier
                    .weight(1f),
                glycemia = glycemia,
                glycemiaError = glycemiaError,
                placeholder = Res.string.pre_prandial
            )
            GlycemiaInputField(
                modifier = Modifier
                    .weight(1f),
                glycemia = postPrandialGlycemia,
                glycemiaError = postPrandialGlycemiaError,
                placeholder = Res.string.post_prandial
            )
        }
    }
}

@Composable
private fun GlycemiaInputField(
    modifier: Modifier,
    glycemia: MutableState<String>,
    glycemiaError: MutableState<Boolean>,
    placeholder: StringResource,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(placeholder),
            style = AppTypography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .height(InputFieldHeight),
            width = 150.dp,
            value = glycemia,
            placeholder = stringResource(Res.string.blood_sugar_placeholder),
            isError = glycemiaError,
            shape = InputFieldShape,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )
    }
}
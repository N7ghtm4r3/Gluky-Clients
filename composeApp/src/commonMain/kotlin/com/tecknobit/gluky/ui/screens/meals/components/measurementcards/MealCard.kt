@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.meals.components.measurementcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeRow
import com.tecknobit.gluky.displayFontFamily
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.icons.CollapseAll
import com.tecknobit.gluky.ui.screens.meals.GlycemiaLevelBadge
import com.tecknobit.gluky.ui.screens.meals.components.FillItemButton
import com.tecknobit.gluky.ui.screens.meals.components.formdialogs.MealFormDialog
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.data.Meal.Companion.levelColor
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import com.tecknobit.refy.ui.icons.ExpandAll
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.administered
import gluky.composeapp.generated.resources.fill_meal
import gluky.composeapp.generated.resources.insulin_units
import gluky.composeapp.generated.resources.no_insulin_needed
import gluky.composeapp.generated.resources.post_prandial_measurement
import gluky.composeapp.generated.resources.pre_prandial_measurement
import gluky.composeapp.generated.resources.show_meal_content
import gluky.composeapp.generated.resources.what_i_ate
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private val quantityRegex = Regex("""\((.*?)\)""")

@Composable
fun MealCard(
    viewModel: MealsScreenViewModel,
    meal: Meal,
) {
    val mealContentDisplayed = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = GlukyCardColors
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
        ) {
            CardHeader(
                viewModel = viewModel,
                meal = meal,
                mealContentDisplayed = mealContentDisplayed
            )
            CardContent(
                meal = meal,
                mealContentDisplayed = mealContentDisplayed
            )
        }
    }
}

@Composable
private fun CardHeader(
    viewModel: MealsScreenViewModel,
    meal: Meal,
    mealContentDisplayed: MutableState<Boolean>,
) {
    CardHeaderContent(
        item = meal,
        type = meal.type,
        endContent = {
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = meal.content.value.isNotEmpty()
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(32.dp),
                        onClick = { mealContentDisplayed.value = !mealContentDisplayed.value }
                    ) {
                        Icon(
                            imageVector = if (mealContentDisplayed.value)
                                CollapseAll
                            else
                                ExpandAll,
                            contentDescription = stringResource(string.show_meal_content)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                )
                FillItemButton(
                    contentDescription = string.fill_meal,
                    fillDialog = { fillMeal ->
                        MealFormDialog(
                            show = fillMeal,
                            viewModel = viewModel,
                            meal = meal
                        )
                    }
                )
            }
        }
    )
}

@Composable
private fun CardContent(
    meal: Meal,
    mealContentDisplayed: MutableState<Boolean>,
) {
    CardContentImpl(
        item = meal,
        type = meal.type,
        filledContent = {
            FilledMeal(
                meal = meal,
                mealContentDisplayed = mealContentDisplayed
            )
        }
    )
}

@Composable
private fun FilledMeal(
    meal: Meal,
    mealContentDisplayed: MutableState<Boolean>,
) {
    Column {
        GlycemiaStatus(
            meal = meal
        )
        AnimatedVisibility(
            visible = !meal.isNotFilledYet
        ) {
            AdministeredUnits(
                insulinUnits = meal.insulinUnits.value
            )
        }
        MealContent(
            meal = meal,
            mealContentDisplayed = mealContentDisplayed
        )
    }
}

@Composable
private fun GlycemiaStatus(
    meal: Meal,
) {
    val density = LocalDensity.current
    JetLimeRow(
        modifier = Modifier
            .fillMaxWidth(),
        itemsList = ItemsList(meal.glycemiaTrend),
        style = JetLimeDefaults.rowStyle(
            lineBrush = Brush.linearGradient(
                colors = listOf(
                    meal.glycemia.value.levelColor(),
                    meal.postPrandialGlycemia.value.levelColor()
                )
            ),
            itemSpacing = with(density) {
                meal.glycemiaGap.toDp()
            },
            contentDistance = 10.dp
        )
    ) { index, glycemia, position ->
        GlycemiaLevel(
            glycemia = glycemia,
            position = position,
            isPrePrandial = index == 0
        )
    }
}

@Composable
private fun GlycemiaLevel(
    glycemia: Int,
    position: EventPosition,
    isPrePrandial: Boolean,
) {
    if (glycemia != -1) {
        val levelColor = glycemia.levelColor()
        JetLimeEvent(
            style = JetLimeEventDefaults.eventStyle(
                position = position,
                pointRadius = 10.dp,
                pointAnimation = null,
                pointStrokeColor = levelColor,
                pointFillColor = levelColor
            )
        ) {
            val scope = rememberCoroutineScope()
            val tooltipState = rememberTooltipState()
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                state = tooltipState,
                tooltip = {
                    PlainTooltip {
                        Text(
                            text = stringResource(
                                if (isPrePrandial)
                                    string.pre_prandial_measurement
                                else
                                    string.post_prandial_measurement
                            )
                        )
                    }
                },
                content = {
                    GlycemiaLevelBadge(
                        // TODO: TO ADOPT THE BUILT-IN CALLBACK WHEN INTEGRATED
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape( // TODO: REMOVE WHEN BUILT-IN CALLBACK INTEGRATED
                                    size = 8.dp
                                )
                            )
                            .clickable {
                                scope.launch {
                                    tooltipState.show()
                                }
                            },
                        glycemia = glycemia
                    )
                }
            )
        }
    }
}

@Composable
private fun AdministeredUnits(
    insulinUnits: Int,
) {
    val insulinUnitsText = formatInsulinUnits(
        insulinUnits = insulinUnits
    )
    Text(
        modifier = Modifier
            .heightIn(
                min = 35.dp
            )
            .padding(
                top = 10.dp
            ),
        text = insulinUnitsText,
        style = AppTypography.bodyLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun formatInsulinUnits(
    insulinUnits: Int,
): AnnotatedString {
    if (insulinUnits == -1)
        return AnnotatedString(stringResource(string.no_insulin_needed))
    val primaryColor = MaterialTheme.colorScheme.primary
    val administered = pluralStringResource(
        resource = Res.plurals.administered,
        insulinUnits
    )
    val insulinUnitsText = pluralStringResource(
        resource = Res.plurals.insulin_units,
        insulinUnits
    )
    return remember(insulinUnits) {
        buildAnnotatedString {
            append(administered)
            append(" ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor
                )
            ) {
                append(insulinUnits.toString())
            }
            append(" ")
            append(insulinUnitsText)
        }
    }
}

@Composable
private fun MealContent(
    meal: Meal,
    mealContentDisplayed: MutableState<Boolean>,
) {
    AnimatedVisibility(
        visible = mealContentDisplayed.value
    ) {
        Column {
            SectionTitle(
                title = string.what_i_ate
            )
            val mealContent = formatMealContent(
                mealContent = meal.content.value
            )
            Text(
                text = mealContent
            )
        }
    }
}

@Composable
private fun formatMealContent(
    mealContent: String,
): AnnotatedString {
    val primaryColor = MaterialTheme.colorScheme.primary
    return remember(mealContent) {
        buildAnnotatedString {
            var lastIndex = 0
            for (match in quantityRegex.findAll(mealContent)) {
                val start = match.range.first
                val end = match.range.last + 1
                append(mealContent.substring(lastIndex, start))
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = primaryColor,
                        fontFamily = displayFontFamily
                    )
                ) {
                    append(mealContent.substring(start, end))
                }
                lastIndex = end
            }
            if (lastIndex < mealContent.length)
                append(mealContent.substring(lastIndex))
        }
    }
}
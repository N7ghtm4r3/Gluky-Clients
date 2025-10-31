@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.measurements.components.measurementcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeRow
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.gluky.displayFontFamily
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.components.ToggleButton
import com.tecknobit.gluky.ui.screens.measurements.components.FillItemButton
import com.tecknobit.gluky.ui.screens.measurements.components.GlycemiaLevelBadge
import com.tecknobit.gluky.ui.screens.measurements.components.formdialogs.MealFormDialog
import com.tecknobit.gluky.ui.screens.measurements.data.types.Meal
import com.tecknobit.gluky.ui.screens.measurements.data.types.Meal.Companion.levelColor
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.fill_meal
import gluky.composeapp.generated.resources.post_prandial_measurement
import gluky.composeapp.generated.resources.pre_prandial_measurement
import gluky.composeapp.generated.resources.show_meal_content
import gluky.composeapp.generated.resources.what_i_ate
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * `quantityRegex` the regex used to format the quantity value in an [AnnotatedString]
 */
private val quantityRegex = Regex("""\((.*?)\)""")

/**
 * Custom [Card] used to display a [Meal] measurement details
 *
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal to display on the card
 */
@Composable
fun MealCard(
    viewModel: MeasurementsScreenViewModel,
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

/**
 * Header of the [MealCard] component
 *
 * @param viewModel The support viewmodel of the screen
 * @param meal The meal to display on the card
 * @param mealContentDisplayed Whether the [MealContent] section is displayed
 */
@Composable
private fun CardHeader(
    viewModel: MeasurementsScreenViewModel,
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
                    ToggleButton(
                        expanded = mealContentDisplayed,
                        contentDescription = string.show_meal_content
                    )
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

/**
 * The content of the [MealCard] component
 *
 * @param meal The meal to display on the card
 * @param mealContentDisplayed Whether the [MealContent] section is displayed
 */
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

/**
 * The content displayed when the [meal] is filled
 *
 * @param meal The meal displayed on the card
 * @param mealContentDisplayed Whether the [MealContent] section is displayed
 */
@Composable
private fun FilledMeal(
    meal: Meal,
    mealContentDisplayed: MutableState<Boolean>,
) {
    Column {
        GlycemiaStatus(
            meal = meal
        )
        AdministeredInsulinUnits(
            insulinUnits = meal.insulinUnits.value
        )
        MealContent(
            meal = meal,
            mealContentDisplayed = mealContentDisplayed
        )
    }
}

/**
 * Section where are displayed the status of the [Meal.glycemia] and [Meal.postPrandialGlycemia]
 * values using a [JetLimeRow]
 *
 * @param meal The meal displayed on the card
 */
@Composable
private fun GlycemiaStatus(
    meal: Meal,
) {
    val density = LocalDensity.current
    JetLimeRow(
        modifier = Modifier
            .fillMaxWidth(),
        itemsList = ItemsList(meal.glycemicTrend),
        style = JetLimeDefaults.rowStyle(
            lineBrush = Brush.linearGradient(
                colors = listOf(
                    meal.glycemia.value.levelColor(),
                    meal.postPrandialGlycemia.value.levelColor()
                )
            ),
            itemSpacing = with(density) {
                meal.glycemicGap.toDp()
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

/**
 * Component used to display the level of a glycemic value
 *
 * @param glycemia The glycemic value
 * @param position The position inside the [JetLimeRow] container
 * @param isPrePrandial Whether the glycemic value is related to a pre-prandial value
 */
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
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
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
                        glycemia = glycemia,
                        onClick = {
                            scope.launch {
                                tooltipState.show()
                            }
                        }
                    )
                }
            )
        }
    }
}

/**
 * The content of the [MealCard] component
 *
 * @param meal The meal displayed on the card
 * @param mealContentDisplayed Whether the [MealContent] section is displayed
 */
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

/**
 * Method used to format as [AnnotatedString] the content of a meal
 *
 * @param mealContent the content of a meal to format
 *
 * @return the meal content formatted as [AnnotatedString]
 */
@Returner
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
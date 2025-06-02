@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.time.TimeFormatter.EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.helpers.asMonth
import com.tecknobit.gluky.ui.components.MeasurementTitle
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.components.ToggleButton
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaPoint
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.ChartLine1Dark
import com.tecknobit.gluky.ui.theme.ChartLine1Light
import com.tecknobit.gluky.ui.theme.ChartLine2Dark
import com.tecknobit.gluky.ui.theme.ChartLine2Light
import com.tecknobit.gluky.ui.theme.ChartLine3Dark
import com.tecknobit.gluky.ui.theme.ChartLine3Light
import com.tecknobit.gluky.ui.theme.ChartLine4Dark
import com.tecknobit.gluky.ui.theme.ChartLine4Light
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.afternoon_snack_info_chart
import gluky.composeapp.generated.resources.average_glycemia
import gluky.composeapp.generated.resources.basal_insulin_info_chart
import gluky.composeapp.generated.resources.breakfast_info_chart
import gluky.composeapp.generated.resources.dinner_info_chart
import gluky.composeapp.generated.resources.first_week
import gluky.composeapp.generated.resources.fourth_week
import gluky.composeapp.generated.resources.higher_glycemia
import gluky.composeapp.generated.resources.info_chart
import gluky.composeapp.generated.resources.lower_glycemia
import gluky.composeapp.generated.resources.lunch_info_chart
import gluky.composeapp.generated.resources.morning_snack_info_chart
import gluky.composeapp.generated.resources.second_week
import gluky.composeapp.generated.resources.show_statistics
import gluky.composeapp.generated.resources.statistics
import gluky.composeapp.generated.resources.third_week
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val AxisProperties = GridProperties.AxisProperties(
    style = StrokeStyle.Dashed()
)

private val LabelStyle
    @Composable get() = AppTypography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

private val lightLineColors = arrayOf(
    ChartLine1Light,
    ChartLine2Light,
    ChartLine3Light,
    ChartLine4Light
)

private val darkLineColors = arrayOf(
    ChartLine1Dark,
    ChartLine2Dark,
    ChartLine3Dark,
    ChartLine4Dark
)

@Composable
fun GlycemicTrend(
    viewModel: AnalysesScreenViewModel,
    type: MeasurementType,
    glycemicTrendData: GlycemicTrendData,
    glycemicTrendPeriod: GlycemicTrendPeriod,
    glycemicTrendGroupingDay: GlycemicTrendGroupingDay?,
) {
    Card(
        modifier = Modifier
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
        ) {
            val colors = if (applyDarkTheme())
                darkLineColors
            else
                lightLineColors
            val statsDisplayed = remember { mutableStateOf(false) }
            ChartHeader(
                type = type,
                statsDisplayed = statsDisplayed,
                glycemicTrendData = glycemicTrendData,
                colors = colors
            )
            var chartWidth by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            val chartData = remember(
                chartWidth,
                glycemicTrendPeriod,
                glycemicTrendGroupingDay,
                viewModel.rangePickerState.selectedStartDateMillis,
                viewModel.rangePickerState.selectedEndDateMillis
            ) {
                glycemicTrendData.toChartData(
                    colors = colors
                )
            }
            LineChart(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    )
                    .fillMaxWidth()
                    .height(250.dp)
                    .onGloballyPositioned {
                        with(density) {
                            chartWidth = it.size.width.toDp()
                        }
                    },
                labelHelperProperties = LabelHelperProperties(
                    enabled = false,
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = LabelStyle,
                    contentBuilder = { it.format(0) }
                ),
                gridProperties = GridProperties(
                    xAxisProperties = AxisProperties,
                    yAxisProperties = AxisProperties
                ),
                dividerProperties = DividerProperties(
                    enabled = false
                ),
                popupProperties = PopupProperties(
                    textStyle = TextStyle.Default.copy(
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    contentBuilder = { dataIndex, valueIndex, value ->
                        useContentBuilder(
                            glycemicTrendData = glycemicTrendData,
                            dataIndex = dataIndex,
                            valueIndex = valueIndex,
                            value = value
                        )
                    }
                ),
                animationMode = AnimationMode.Together(
                    delayBuilder = { it * 500L }
                ),
                data = chartData
            )
            TrendStats(
                statsDisplayed = statsDisplayed,
                glycemiaTrendData = glycemicTrendData
            )
        }
    }
}

@Composable
private fun ChartHeader(
    type: MeasurementType,
    statsDisplayed: MutableState<Boolean>,
    glycemicTrendData: GlycemicTrendData,
    colors: Array<Color>,
) {
    MeasurementTitle(
        modifier = Modifier
            .padding(
                start = 6.dp
            ),
        type = type,
        endContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToggleButton(
                    expanded = statsDisplayed,
                    contentDescription = string.show_statistics
                )
                InfoChart(
                    type = type
                )
            }
        }
    )
    ChartLegend(
        glycemicTrendData = glycemicTrendData,
        colors = colors
    )
}

@Composable
private fun InfoChart(
    type: MeasurementType,
) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        state = tooltipState,
        tooltip = {
            RichTooltip {
                Text(
                    text = stringResource(type.infoText())
                )
            }
        },
        content = {
            IconButton(
                onClick = {
                    scope.launch {
                        tooltipState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(string.info_chart)
                )
            }
        }
    )
}

private fun MeasurementType.infoText(): StringResource {
    return when (this) {
        BREAKFAST -> string.breakfast_info_chart
        MORNING_SNACK -> string.morning_snack_info_chart
        LUNCH -> string.lunch_info_chart
        AFTERNOON_SNACK -> string.afternoon_snack_info_chart
        DINNER -> string.dinner_info_chart
        BASAL_INSULIN -> string.basal_insulin_info_chart
    }
}

@Composable
@NonRestartableComposable
private fun ChartLegend(
    glycemicTrendData: GlycemicTrendData,
    colors: Array<Color>,
) {
    val labels = glycemicTrendData.getLabels()
    if (labels.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            itemsIndexed(
                items = labels,
                key = { _, label -> label }
            ) { index, label ->
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(colors[index])
                    )
                    Text(
                        text = label,
                        style = LabelStyle
                    )
                }
            }
        }
    }
}

@Returner
@Composable
private fun GlycemicTrendData.getLabels(): Array<String> {
    return when (labelType) {
        COMPUTE_WEEK -> {
            arrayOf(
                stringResource(string.first_week),
                stringResource(string.second_week),
                stringResource(string.third_week),
                stringResource(string.fourth_week)
            )
        }

        COMPUTE_MONTH -> {
            val dates = mutableListOf<Long>()
            sets.forEach { dataSet ->
                val firstEntry = dataSet.first()
                dates.add(firstEntry.date)
            }
            return Array(dates.size) { index -> stringResource(dates[index].asMonth()) }
        }

        else -> emptyArray()
    }
}

private fun GlycemicTrendData.toChartData(
    colors: Array<Color>,
): List<Line> {
    val lines = mutableListOf<Line>()
    sets.forEachIndexed { index, points ->
        val lineColor = colors[index]
        lines.add(
            Line(
                label = "",
                values = points.map { it.value },
                color = SolidColor(lineColor),
                curvedEdges = false,
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(Color.White),
                    strokeWidth = 2.dp,
                    radius = 3.dp,
                    strokeColor = SolidColor(lineColor),
                )
            )
        )
    }
    return lines
}

private fun useContentBuilder(
    glycemicTrendData: GlycemicTrendData,
    dataIndex: Int,
    valueIndex: Int,
    value: Double,
): String {
    val points = glycemicTrendData.getSpecifiedSet(
        index = dataIndex
    )!!
    val pointIndex = if (valueIndex > points.lastIndex)
        points.lastIndex
    else
        valueIndex
    return """
        ${
        points[pointIndex].date.toDateString(
            pattern = EUROPEAN_DATE_PATTERN
        )
    }
        ${value.format(0)}
    """.trimIndent()
}

@Composable
private fun TrendStats(
    statsDisplayed: MutableState<Boolean>,
    glycemiaTrendData: GlycemicTrendData,
) {
    AnimatedVisibility(
        visible = statsDisplayed.value
    ) {
        Column {
            SectionTitle(
                title = string.statistics
            )
            LazyRow(
                contentPadding = PaddingValues(
                    vertical = 5.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    StatsTile(
                        stat = glycemiaTrendData.higherGlycemia,
                        header = string.higher_glycemia
                    )
                }
                item {
                    StatsTile(
                        stat = glycemiaTrendData.lowerGlycemia,
                        header = string.lower_glycemia
                    )
                }
                item {
                    StatsTile(
                        stat = glycemiaTrendData.averageGlycemia,
                        header = string.average_glycemia
                    )
                }
            }
        }
    }
}

@Wrapper
@Composable
private fun StatsTile(
    stat: GlycemiaPoint,
    header: StringResource,
) {
    StatsTile(
        stat = stat.value,
        decimalPlaces = 0,
        header = header,
        extraContent = {
            Text(
                text = stat.date.toDateString(
                    pattern = EUROPEAN_DATE_PATTERN
                ),
                style = AppTypography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
private fun StatsTile(
    stat: Double,
    decimalPlaces: Int = 2,
    header: StringResource,
    extraContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .size(
                width = 125.dp,
                height = 75.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stat.format(
                    decimalPlaces = decimalPlaces
                ),
                style = AppTypography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(header),
                style = AppTypography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            extraContent?.invoke(this)
        }
    }
}
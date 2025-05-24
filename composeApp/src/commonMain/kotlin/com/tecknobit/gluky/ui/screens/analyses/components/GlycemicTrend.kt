@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.equinoxcore.time.TimeFormatter.EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.helpers.asMonth
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.meals.components.MeasurementTitle
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.ChartLine1Dark
import com.tecknobit.gluky.ui.theme.ChartLine1Light
import com.tecknobit.gluky.ui.theme.ChartLine2Dark
import com.tecknobit.gluky.ui.theme.ChartLine2Light
import com.tecknobit.gluky.ui.theme.ChartLine3Dark
import com.tecknobit.gluky.ui.theme.ChartLine3Light
import com.tecknobit.gluky.ui.theme.ChartLine4Dark
import com.tecknobit.gluky.ui.theme.ChartLine4Light
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.MeasurementType
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.first_week
import gluky.composeapp.generated.resources.fourth_week
import gluky.composeapp.generated.resources.second_week
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
    type: MeasurementType,
    glycemicTrendData: GlycemicTrendData,
    glycemicTrendPeriod: GlycemicTrendPeriod,
    glycemicTrendGroupingDay: GlycemicTrendGroupingDay?,
) {
    Card(
        colors = GlukyCardColors
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
            var chartWidth by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            val chartData = remember(chartWidth, glycemicTrendPeriod, glycemicTrendGroupingDay) {
                glycemicTrendData.toChartData(
                    colors = colors
                )
            }
            ChartHeader(
                type = type,
                glycemicTrendData = glycemicTrendData,
                colors = colors
            )
            LineChart(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        top = 10.dp,
                        bottom = 11.dp
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
        }
    }
}

@Composable
private fun ChartHeader(
    type: MeasurementType,
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
            val tooltipState = rememberTooltipState()
            val scope = rememberCoroutineScope()
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                state = tooltipState,
                tooltip = {
                    PlainTooltip {
                        Text(
                            text = "aaa"
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
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    )
    ChartLegend(
        glycemicTrendData = glycemicTrendData,
        colors = colors
    )
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
                stringResource(Res.string.first_week),
                stringResource(Res.string.second_week),
                stringResource(Res.string.third_week),
                stringResource(Res.string.fourth_week)
            )
        }

        COMPUTE_MONTH -> {
            val dates = mutableListOf<Long>()
            sets.forEach { dataSet ->
                val firstEntry = dataSet.points.first()
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
    sets.forEachIndexed { index, dataSet ->
        val set = dataSet.points
        val lineColor = colors[index]
        lines.add(
            Line(
                label = "",
                values = set.map { it.value.toDouble() },
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
    val dataSet = glycemicTrendData.getSpecifiedSet(
        index = dataIndex
    )!!
    val set = dataSet.points
    val pointIndex = if (valueIndex > set.lastIndex)
        set.lastIndex
    else
        dataIndex
    return """
        ${
        set[pointIndex].date.toDateString(
            pattern = EUROPEAN_DATE_PATTERN
        )
    }
        ${value.format(0)}
    """.trimIndent()
}
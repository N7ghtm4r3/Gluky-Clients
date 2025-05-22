package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.gluky.helpers.asMonth
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
import com.tecknobit.gluky.ui.theme.EmptyStateTitleStyle
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.choose_another_period
import gluky.composeapp.generated.resources.empty_sets_dark
import gluky.composeapp.generated.resources.empty_sets_light
import gluky.composeapp.generated.resources.first_week
import gluky.composeapp.generated.resources.fourth_week
import gluky.composeapp.generated.resources.no_data_available
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
import org.jetbrains.compose.resources.stringResource

private val AxisProperties = GridProperties.AxisProperties(
    style = StrokeStyle.Dashed()
)

private val LabelStyle
    @Composable get() = AppTypography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

private val PopupProperties = PopupProperties(
    textStyle = TextStyle.Default.copy(
        color = Color.White,
        fontSize = 12.sp
    ),
    //contentBuilder = {  }
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
    glycemicTrendData: GlycemicTrendData,
    glycemicTrendPeriod: GlycemicTrendPeriod,
    glycemicTrendGroupingDay: GlycemicTrendGroupingDay,
) {
    if (glycemicTrendData.sets.isEmpty()) {
        EmptySets(
            viewModel = viewModel,
            glycemicTrendPeriod = glycemicTrendPeriod,
            glycemicTrendGroupingDay = glycemicTrendGroupingDay
        )
    } else {
        val colors = if (applyDarkTheme())
            darkLineColors
        else
            lightLineColors
        var chartWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        val labelsHelper = glycemicTrendData.getLabels()
        val chartData = remember(chartWidth, glycemicTrendPeriod, glycemicTrendGroupingDay) {
            glycemicTrendData.toChartData(
                colors = colors,
                labels = labelsHelper
            )
        }
        LineChart(
            modifier = Modifier
                .padding(
                    top = 16.dp
                )
                .fillMaxWidth()
                .height(250.dp)
                .onGloballyPositioned {
                    with(density) {
                        chartWidth = it.size.width.toDp()
                    }
                },
            labelHelperProperties = LabelHelperProperties(
                enabled = glycemicTrendData.labelType != null,
                textStyle = LabelStyle
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
            popupProperties = PopupProperties,
            animationMode = AnimationMode.Together(
                delayBuilder = { it * 500L }
            ),
            data = chartData
        )
    }
}

@Composable
private fun EmptySets(
    viewModel: AnalysesScreenViewModel,
    glycemicTrendPeriod: GlycemicTrendPeriod,
    glycemicTrendGroupingDay: GlycemicTrendGroupingDay,
) {
    val title = stringResource(Res.string.no_data_available)
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        lightResource = Res.drawable.empty_sets_light,
        darkResource = Res.drawable.empty_sets_dark,
        title = title,
        contentDescription = title,
        titleStyle = EmptyStateTitleStyle,
        action = {
            GlycemicTrendPeriodSelector(
                viewModel = viewModel,
                glycemicTrendPeriod = glycemicTrendPeriod
            )
        },
        subTitle = stringResource(Res.string.choose_another_period),
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumWidthExpandedHeight = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 250.dp }
        )
    )
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
                val firstEntry = dataSet.set.first()
                dates.add(firstEntry.date)
            }
            return Array(dates.size) { index -> stringResource(dates[index].asMonth()) }
        }

        else -> arrayOf("")
    }
}

private fun GlycemicTrendData.toChartData(
    colors: Array<Color>,
    labels: Array<String>,
): List<Line> {
    val lines = mutableListOf<Line>()
    sets.forEachIndexed { index, dataSet ->
        val set = dataSet.set
        val lineColor = colors[index]
        lines.add(
            Line(
                label = labels[index],
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
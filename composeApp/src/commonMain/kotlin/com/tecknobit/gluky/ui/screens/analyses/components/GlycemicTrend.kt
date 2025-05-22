@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.currentSizeClass
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.theme.ChartLine1Dark
import com.tecknobit.gluky.ui.theme.ChartLine1Light
import com.tecknobit.gluky.ui.theme.ChartLine2Dark
import com.tecknobit.gluky.ui.theme.ChartLine2Light
import com.tecknobit.gluky.ui.theme.ChartLine3Dark
import com.tecknobit.gluky.ui.theme.ChartLine3Light
import com.tecknobit.gluky.ui.theme.ChartLine4Dark
import com.tecknobit.gluky.ui.theme.ChartLine4Light
import com.tecknobit.gluky.ui.theme.ChartLine5Dark
import com.tecknobit.gluky.ui.theme.ChartLine5Light
import com.tecknobit.gluky.ui.theme.ChartLine6Dark
import com.tecknobit.gluky.ui.theme.ChartLine6Light
import com.tecknobit.gluky.ui.theme.EmptyStateTitleStyle
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.choose_another_period
import gluky.composeapp.generated.resources.empty_sets_dark
import gluky.composeapp.generated.resources.empty_sets_light
import gluky.composeapp.generated.resources.no_data_available
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import org.jetbrains.compose.resources.stringResource

private val axisProperties = GridProperties.AxisProperties(
    style = StrokeStyle.Dashed()
)

private val popupProperties = PopupProperties(
    textStyle = TextStyle.Default.copy(
        color = Color.White,
        fontSize = 12.sp
    ),
    //contentBuilder = {  }
)

private val lightLineColors = arrayOf(
    ChartLine1Light, ChartLine2Light, ChartLine3Light,
    ChartLine4Light, ChartLine5Light, ChartLine6Light
)

private val darkLineColors = arrayOf(
    ChartLine1Dark, ChartLine2Dark, ChartLine3Dark,
    ChartLine4Dark, ChartLine5Dark, ChartLine6Dark
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
        val chartData =
            remember(currentSizeClass(), glycemicTrendPeriod, glycemicTrendGroupingDay) {
            glycemicTrendData.toChartData(
                colors = colors
            )
        }
        LineChart(
            modifier = Modifier
                /*.onGloballyPositioned {
                    with(density) {
                        width = it.size.width.toDp()
                    }
                }*/
                .fillMaxWidth()
                .height(150.dp),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = false
            ),
            gridProperties = GridProperties(
                xAxisProperties = axisProperties,
                yAxisProperties = axisProperties
            ),
            dividerProperties = DividerProperties(
                enabled = false
            ),
            popupProperties = popupProperties,
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

private fun GlycemicTrendData.toChartData(
    colors: Array<Color>,
): List<Line> {
    val lines = mutableListOf<Line>()
    sets.forEachIndexed { index, dataSet ->
        val set = dataSet.set
        val lineColor = colors[index]
        lines.add(
            Line(
                label = "Temperature",
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
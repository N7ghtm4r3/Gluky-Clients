@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.currentSizeClass
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendData
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
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.empty_sets_dark
import gluky.composeapp.generated.resources.empty_sets_light
import gluky.composeapp.generated.resources.no_data_available
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties
import org.jetbrains.compose.resources.stringResource

private val lightLineColors = arrayOf(
    ChartLine1Light, ChartLine2Light, ChartLine3Light,
    ChartLine4Light, ChartLine5Light, ChartLine6Light
)

private val darkLineColors = arrayOf(
    ChartLine1Dark, ChartLine2Dark, ChartLine3Dark,
    ChartLine4Dark, ChartLine5Dark, ChartLine6Dark
)

@Composable
fun GlycemiaTrend(
    viewModel: AnalysesScreenViewModel,
    glycemiaTrendData: GlycemiaTrendData,
) {
    if (glycemiaTrendData.sets.isEmpty()) {
        EmptySets(
            viewModel = viewModel
        )
    } else {
        val colors = if (applyDarkTheme())
            darkLineColors
        else
            lightLineColors
        val chartData = remember(currentSizeClass()) {
            glycemiaTrendData.toChartData(
                colors = colors
            )
        }
        LineChart(
            modifier = Modifier
                .fillMaxWidth(),
            data = chartData,
            zeroLineProperties = ZeroLineProperties(
                enabled = true,
                color = SolidColor(Color.Red),
                zType = ZeroLineProperties.ZType.Above
            ),
            minValue = -20.0,
            maxValue = 100.0
        )
    }
}

@Composable
private fun EmptySets(
    viewModel: AnalysesScreenViewModel,
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
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumWidthExpandedHeight = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 250.dp }
        )
    )
}

private fun GlycemiaTrendData.toChartData(
    colors: Array<Color>,
): List<Line> {
    val lines = mutableListOf<Line>()
    sets.forEachIndexed { index, dataSet ->
        lines.add(
            Line(
                label = "Temperature",
                values = dataSet.set.map { it.value },
                color = SolidColor(colors[index])
            )
        )
    }
    return lines
}
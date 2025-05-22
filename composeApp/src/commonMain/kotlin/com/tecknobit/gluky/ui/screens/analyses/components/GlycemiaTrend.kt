package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendData
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Composable
fun GlycemiaTrend(
    viewModel: AnalysesScreenViewModel,
    glycemiaTrendData: GlycemiaTrendData,
) {
    LineChart(
        modifier = Modifier
            .fillMaxWidth(),
        data = remember {
            listOf(
                Line(
                    label = "Temperature",
                    values = listOf(28.0, 41.0, -5.0, 10.0, 35.0),
                    color = SolidColor(Color.Red)
                )
            )
        },
        zeroLineProperties = ZeroLineProperties(
            enabled = true,
            color = SolidColor(Color.Red),
            zType = ZeroLineProperties.ZType.Above
        ),
        minValue = -20.0,
        maxValue = 100.0
    )
}
package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.gluky.ui.theme.EmptyStateTitleStyle
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.choose_another_period
import gluky.composeapp.generated.resources.empty_sets_dark
import gluky.composeapp.generated.resources.empty_sets_light
import gluky.composeapp.generated.resources.no_data_available
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyTrendData(
    viewModel: AnalysesScreenViewModel,
    glycemicTrendPeriod: GlycemicTrendPeriod,
) {
    val title = stringResource(Res.string.no_data_available)
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
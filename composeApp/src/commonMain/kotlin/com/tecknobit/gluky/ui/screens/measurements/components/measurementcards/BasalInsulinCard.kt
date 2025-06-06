package com.tecknobit.gluky.ui.screens.measurements.components.measurementcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.screens.measurements.components.FillItemButton
import com.tecknobit.gluky.ui.screens.measurements.components.GlycemiaLevelBadge
import com.tecknobit.gluky.ui.screens.measurements.components.formdialogs.BasalInsulinFormDialog
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.data.types.BasalInsulin
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.theme.GlukyCardColors
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import gluky.composeapp.generated.resources.Res.string
import gluky.composeapp.generated.resources.fill_basal_insulin
import gluky.composeapp.generated.resources.glycemic_value

@Composable
fun BasalInsulinCard(
    viewModel: MeasurementsScreenViewModel,
    dailyMeasurements: DailyMeasurements,
) {
    val basalInsulin = dailyMeasurements.basalInsulin
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = GlukyCardColors
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            CardHeader(
                viewModel = viewModel,
                basalInsulin = basalInsulin
            )
            CardContent(
                basalInsulin = basalInsulin
            )
        }
    }
}

@Composable
private fun CardHeader(
    viewModel: MeasurementsScreenViewModel,
    basalInsulin: BasalInsulin,
) {
    CardHeaderContent(
        item = basalInsulin,
        type = BASAL_INSULIN,
        endContent = {
            FillItemButton(
                contentDescription = string.fill_basal_insulin,
                fillDialog = { fill ->
                    BasalInsulinFormDialog(
                        show = fill,
                        viewModel = viewModel,
                        basalInsulin = basalInsulin
                    )
                }
            )
        }
    )
}

@Composable
private fun CardContent(
    basalInsulin: BasalInsulin,
) {
    CardContentImpl(
        item = basalInsulin,
        type = BASAL_INSULIN,
        filledContent = {
            FilledBasalInsulin(
                basalInsulin = basalInsulin
            )
        }
    )
}

@Composable
private fun FilledBasalInsulin(
    basalInsulin: BasalInsulin,
) {
    Column {
        GlycemiaLevelSection(
            basalInsulin = basalInsulin
        )
        AdministeredInsulinUnits(
            insulinUnits = basalInsulin.insulinUnits.value
        )
    }
}

@Composable
private fun GlycemiaLevelSection(
    basalInsulin: BasalInsulin,
) {
    AnimatedVisibility(
        visible = basalInsulin.isGlycemiaFilled
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SectionTitle(
                title = string.glycemic_value
            )
            GlycemiaLevelBadge(
                glycemia = basalInsulin.glycemia.value
            )
        }
    }
}
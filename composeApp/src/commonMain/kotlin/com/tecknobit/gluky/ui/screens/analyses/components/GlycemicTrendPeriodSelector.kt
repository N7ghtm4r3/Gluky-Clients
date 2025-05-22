package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.FOUR_MONTHS
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.THREE_MONTHS
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.four_months
import gluky.composeapp.generated.resources.one_month
import gluky.composeapp.generated.resources.one_week
import gluky.composeapp.generated.resources.three_months
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GlycemicTrendPeriodSelector(
    viewModel: AnalysesScreenViewModel,
    glycemicTrendPeriod: GlycemicTrendPeriod,
) {
    LazyRow {
        items(
            items = GlycemicTrendPeriod.entries.toList(),
            key = { period -> period }
        ) { period ->
            TextButton(
                onClick = {
                    viewModel.selectGlycemicTrendPeriod(
                        period = period
                    )
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (period == glycemicTrendPeriod)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Text(
                    text = stringResource(period.text())
                )
            }
        }
    }
}

@Returner
private fun GlycemicTrendPeriod.text(): StringResource {
    return when (this) {
        ONE_WEEK -> Res.string.one_week
        ONE_MONTH -> Res.string.one_month
        THREE_MONTHS -> Res.string.three_months
        FOUR_MONTHS -> Res.string.four_months
    }
}
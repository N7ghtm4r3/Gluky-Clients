package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.fill_day
import gluky.composeapp.generated.resources.no_filled_day_dark
import gluky.composeapp.generated.resources.no_filled_day_light
import gluky.composeapp.generated.resources.unfilled_day
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnfilledDay(
    viewModel: MealsScreenViewModel,
) {
    val title = stringResource(Res.string.unfilled_day)
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        lightResource = Res.drawable.no_filled_day_light,
        darkResource = Res.drawable.no_filled_day_dark,
        useDarkResource = applyDarkTheme(),
        contentDescription = title,
        title = title,
        titleStyle = AppTypography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        action = {
            Button(
                onClick = { viewModel.fillDay() }
            ) {
                Text(
                    text = stringResource(Res.string.fill_day),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumWidthExpandedHeight = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 250.dp }
        )
    )
}
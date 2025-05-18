package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.applyDarkTheme
import com.tecknobit.glukycore.enums.MeasurementType
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.fill_day
import gluky.composeapp.generated.resources.no_filled_day_dark
import gluky.composeapp.generated.resources.no_filled_day_light
import gluky.composeapp.generated.resources.unfilled_day
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealDay(
    viewModel: MealsScreenViewModel,
    horizontalPadding: Dp = 0.dp,
    mealDay: MealDayData?,
) {
    AnimatedVisibility(
        visible = mealDay == null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        NoFilledDay(
            viewModel = viewModel
        )
    }
    AnimatedVisibility(
        visible = mealDay != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        mealDay?.let {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .animateContentSize()
                    .navigationBarsPadding(),
                columns = StaggeredGridCells.Adaptive(
                    minSize = 350.dp
                ),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = horizontalPadding
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp
            ) {
                items(
                    items = MeasurementType.meals(),
                    key = { type -> type }
                ) { type ->
                    MealCard(
                        viewModel = viewModel,
                        meal = mealDay.getMealByType(
                            type = type
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NoFilledDay(
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

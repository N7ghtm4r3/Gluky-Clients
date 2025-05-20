package com.tecknobit.gluky.ui.screens.meals

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.components.BadgeText
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.gluky.ui.screens.meals.data.Meal.Companion.levelColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GlycemiaLevelBadge(
    modifier: Modifier = Modifier,
    glycemia: Int,
) {
    val levelColor = glycemia.levelColor()
    BadgeText(
        modifier = modifier,
        badgeText = glycemia.toString(),
        badgeColor = levelColor,
        textColor = getContrastColor(
            backgroundColor = levelColor
        )
    )
}
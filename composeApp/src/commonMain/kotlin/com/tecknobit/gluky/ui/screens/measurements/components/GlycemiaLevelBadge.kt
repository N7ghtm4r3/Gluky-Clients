package com.tecknobit.gluky.ui.screens.measurements.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.components.BadgeText
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.gluky.ui.screens.measurements.data.types.Meal.Companion.levelColor

/**
 * Custom [BadgeText] useful to display the level of a glycemic value
 *
 * @param modifier The modifier to apply to the badge
 * @param glycemia The glycemic value
 * @param onClick The callback to invoke when the user clicked the badge
 */
@Composable
fun GlycemiaLevelBadge(
    modifier: Modifier = Modifier,
    glycemia: Int,
    onClick: (() -> Unit)? = null,
) {
    val levelColor = glycemia.levelColor()
    BadgeText(
        modifier = modifier,
        badgeText = glycemia.toString(),
        badgeColor = levelColor,
        textColor = getContrastColor(
            backgroundColor = levelColor
        ),
        onClick = onClick
    )
}
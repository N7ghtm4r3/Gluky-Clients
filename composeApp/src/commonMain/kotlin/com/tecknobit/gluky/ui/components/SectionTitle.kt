package com.tecknobit.gluky.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.tecknobit.gluky.ui.theme.AppTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Title of a section component
 *
 * @param modifier The modifier to apply to the component
 * @param title The title value
 * @param style The style to apply to the title
 * @param fontWeight The typeface thickness to use when painting the text (e.g., FontWeight.Bold)
 * @param color The color to apply to the title
 */
@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: StringResource,
    style: TextStyle = AppTypography.titleMedium,
    fontWeight: FontWeight? = FontWeight.Bold,
    color: Color = Color.Unspecified,
) {
    Text(
        modifier = modifier,
        text = stringResource(title),
        style = style,
        fontWeight = fontWeight,
        color = color
    )
}
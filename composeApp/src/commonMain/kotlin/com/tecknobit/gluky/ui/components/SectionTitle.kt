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
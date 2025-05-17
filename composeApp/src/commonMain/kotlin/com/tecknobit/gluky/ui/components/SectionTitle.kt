package com.tecknobit.gluky.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.tecknobit.gluky.ui.theme.AppTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: StringResource,
) {
    Text(
        modifier = modifier,
        text = stringResource(title),
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}
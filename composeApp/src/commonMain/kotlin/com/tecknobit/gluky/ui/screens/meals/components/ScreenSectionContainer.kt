package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.gluky.ui.components.SectionTitle
import com.tecknobit.gluky.ui.theme.AppTypography
import org.jetbrains.compose.resources.StringResource

@Composable
@ScreenSection
fun ScreenSectionContainer(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    title: StringResource,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SectionTitle(
            modifier = Modifier
                .padding(
                    top = topPadding,
                    start = 16.dp,
                ),
            title = title,
            style = AppTypography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}
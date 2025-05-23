package com.tecknobit.gluky.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

val InputFieldShape = RoundedCornerShape(
    size = 10.dp
)

val InputFieldHeight = 52.dp

val GlukyCardColors: CardColors
    @Composable get() = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )

val EmptyStateTitleStyle: TextStyle
    @Composable get() = AppTypography.headlineSmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

val DialogShape = RoundedCornerShape(
    size = 16.dp
)

inline fun Modifier.useDialogSize(): Modifier {
    return fillMaxWidth()
        .heightIn(
            max = 750.dp
        )
}
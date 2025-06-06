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

/**
 * `InputFieldShape` the default shape for the input fields
 */
val InputFieldShape = RoundedCornerShape(
    size = 10.dp
)

/**
 * `InputFieldHeight` the default height for the input fields
 */
val InputFieldHeight = 52.dp

/**
 * `GlukyCardColors` the default set of colors to apply to a [androidx.compose.material3.Card] element
 */
val GlukyCardColors: CardColors
    @Composable get() = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )

/**
 * `EmptyStateTitleStyle` the style to apply to the title of the [com.tecknobit.equinoxcompose.components.EmptyState]
 * element
 */
val EmptyStateTitleStyle: TextStyle
    @Composable get() = AppTypography.headlineSmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

/**
 * `DialogShape` the default shape to apply to the dialogs
 */
val DialogShape = RoundedCornerShape(
    size = 16.dp
)

/**
 * Modifier method used apply the size of a dialogs
 *
 * @return the modifier as [Modifier]
 */
inline fun Modifier.useDialogSize(): Modifier {
    return fillMaxWidth()
        .heightIn(
            max = 750.dp
        )
}
package com.tecknobit.gluky.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val InputFieldShape = RoundedCornerShape(
    size = 10.dp
)

val InputFieldHeight = 52.dp

val GlukyCardColors: CardColors
    @Composable get() = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
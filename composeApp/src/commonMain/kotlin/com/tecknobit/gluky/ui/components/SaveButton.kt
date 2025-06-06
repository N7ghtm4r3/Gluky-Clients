package com.tecknobit.gluky.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [Button] used to save an item
 *
 * @param modifier The modifier to apply to the button
 * @param save The save action to execute
 */
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    save: () -> Unit,
) {
    Button(
        modifier = modifier
            .width(100.dp),
        onClick = save
    ) {
        Text(
            text = stringResource(Res.string.save),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
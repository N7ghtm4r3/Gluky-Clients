package com.tecknobit.gluky.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.resources.retry
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [TextButton] used to invoke the [retryAction]
 *
 * @param retryAction The action to retry
 */
@Composable
fun RetryButton(
    retryAction: () -> Unit,
) {
    TextButton(
        onClick = retryAction
    ) {
        Text(
            text = stringResource(com.tecknobit.equinoxcompose.resources.Res.string.retry)
        )
    }
}
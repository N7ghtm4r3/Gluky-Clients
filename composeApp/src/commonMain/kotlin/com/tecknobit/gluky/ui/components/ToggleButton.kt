package com.tecknobit.gluky.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.icons.CollapseAll
import com.tecknobit.gluky.ui.icons.ExpandAll
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ToggleButton(
    expanded: MutableState<Boolean>,
    contentDescription: StringResource,
) {
    IconButton(
        modifier = Modifier
            .size(32.dp),
        onClick = { expanded.value = !expanded.value }
    ) {
        Icon(
            imageVector = if (expanded.value)
                CollapseAll
            else
                ExpandAll,
            contentDescription = stringResource(contentDescription)
        )
    }
}
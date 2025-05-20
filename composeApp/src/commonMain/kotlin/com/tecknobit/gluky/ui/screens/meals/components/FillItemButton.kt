package com.tecknobit.gluky.ui.screens.meals.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.gluky.ui.icons.FillMeal
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FillItemButton(
    contentDescription: StringResource,
    fillDialog: @Composable (MutableState<Boolean>) -> Unit,
) {
    val fill = remember { mutableStateOf(false) }
    IconButton(
        modifier = Modifier
            .size(32.dp),
        onClick = { fill.value = !fill.value }
    ) {
        Icon(
            imageVector = FillMeal,
            contentDescription = stringResource(contentDescription)
        )
    }
    fillDialog(fill)
}
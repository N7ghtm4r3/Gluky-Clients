@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.measurements.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.gluky.helpers.ScreenMonitor
import com.tecknobit.gluky.ui.icons.EditItem
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.daily_notes
import gluky.composeapp.generated.resources.edit_daily_notes
import gluky.composeapp.generated.resources.my_daily_notes
import gluky.composeapp.generated.resources.on_editing
import gluky.composeapp.generated.resources.save
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun DailyNotes(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: MeasurementsScreenViewModel,
    mealDay: DailyMeasurements,
) {
    if (state.isVisible) {
        ScreenMonitor.keepScreenAwake()
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(),
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            val dailyNotes = mealDay.dailyNotes
            val isEditing = remember { mutableStateOf(dailyNotes.isEmpty()) }
            val content = remember { mutableStateOf(dailyNotes) }
            SheetHeader(
                state = state,
                scope = scope,
                viewModel = viewModel,
                isEditing = isEditing,
                dailyNotes = dailyNotes,
                content = content
            )
            NotesContent(
                isEditing = isEditing,
                content = content
            )
        }
    } else
        ScreenMonitor.allowScreenSleeps()
}

@Composable
private fun SheetHeader(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: MeasurementsScreenViewModel,
    isEditing: MutableState<Boolean>,
    dailyNotes: String,
    content: MutableState<String>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                start = 16.dp,
                bottom = 6.dp,
                end = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(Res.string.daily_notes),
                style = AppTypography.titleLarge
            )
            AnimatedVisibility(
                visible = isEditing.value
            ) {
                Text(
                    text = stringResource(Res.string.on_editing),
                    style = AppTypography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    if (isEditing.value) {
                        viewModel.saveDailyNotes(
                            content = content.value,
                            onSave = {
                                isEditing.value = false
                                if (content.value.isEmpty()) {
                                    scope.launch {
                                        state.hide()
                                    }
                                }
                            }
                        )
                    } else
                        isEditing.value = true
                },
                enabled = content.value.isNotEmpty() || dailyNotes.isNotEmpty()
            ) {
                AnimatedContent(
                    targetState = isEditing.value
                ) { isEditingMode ->
                    CurrentModeIcon(
                        isEditing = isEditingMode
                    )
                }
            }
        }
    }
    HorizontalDivider()
}

@Composable
private fun CurrentModeIcon(
    isEditing: Boolean,
) {
    if (isEditing) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = stringResource(Res.string.save)
        )
    } else {
        Icon(
            imageVector = EditItem,
            contentDescription = stringResource(Res.string.edit_daily_notes)
        )
    }
}

@Composable
private fun NotesContent(
    isEditing: MutableState<Boolean>,
    content: MutableState<String>,
) {
    AnimatedContent(
        targetState = isEditing.value,
        transitionSpec = { fadeIn().togetherWith(fadeOut()) }
    ) { isEditingMode ->
        if (isEditingMode) {
            EditNotes(
                content = content
            )
        } else {
            DisplayNotes(
                content = content
            )
        }
    }
}

@Composable
private fun EditNotes(
    content: MutableState<String>,
) {
    EquinoxTextField(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        textFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        value = content,
        placeholder = stringResource(Res.string.my_daily_notes),
        maxLines = Int.MAX_VALUE,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        )
    )
}

@Composable
private fun DisplayNotes(
    content: MutableState<String>,
) {
    val richTextState = rememberRichTextState()
    richTextState.config.linkColor = MaterialTheme.colorScheme.primary
    richTextState.config.orderedListIndent = 4
    richTextState.config.unorderedListIndent = 4
    LaunchedEffect(content.value) {
        richTextState.setMarkdown(content.value)
    }
    RichText(
        modifier = Modifier
            .padding(
                all = 16.dp
            )
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        state = richTextState
    )
}
package com.tecknobit.gluky.ui.screens.analyses.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.resources.confirm
import com.tecknobit.equinoxcompose.resources.dismiss
import com.tecknobit.gluky.ui.components.titleStyle
import com.tecknobit.gluky.ui.icons.Report
import com.tecknobit.gluky.ui.screens.analyses.presentation.AnalysesScreenViewModel
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.create_report
import gluky.composeapp.generated.resources.create_report_warn_text
import gluky.composeapp.generated.resources.creating
import org.jetbrains.compose.resources.stringResource

/**
 * Dialog displayed when the user requests to create a report
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel of the screen
 */
@Composable
fun GlycemicReportDialog(
    show: MutableState<Boolean>,
    viewModel: AnalysesScreenViewModel,
) {
    if (show.value) {
        val creatingReport by viewModel.creatingReport.collectAsState()
        AlertDialog(
            modifier = Modifier
                .widthIn(
                    max = 400.dp
                ),
            icon = {
                Icon(
                    imageVector = Report,
                    contentDescription = null
                )
            },
            onDismissRequest = {
                if (!creatingReport)
                    show.value = false
            },
            title = {
                Text(
                    text = stringResource(Res.string.create_report),
                    style = titleStyle
                )
            },
            text = {
                Text(
                    text = stringResource(Res.string.create_report_warn_text),
                    textAlign = TextAlign.Justify
                )
            },
            dismissButton = {
                AnimatedVisibility(
                    visible = !creatingReport
                ) {
                    TextButton(
                        onClick = { show.value = false }
                    ) {
                        Text(
                            text = stringResource(com.tecknobit.equinoxcompose.resources.Res.string.dismiss)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.createReport(
                            onCreated = { show.value = false }
                        )
                    },
                    enabled = !creatingReport
                ) {
                    Text(
                        text = stringResource(
                            if (creatingReport)
                                Res.string.creating
                            else
                                com.tecknobit.equinoxcompose.resources.Res.string.confirm
                        )
                    )
                }
            }
        )
    }
}
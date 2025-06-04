@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)

package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToasterState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.gluky.helpers.KReviewer
import com.tecknobit.gluky.helpers.openReport
import com.tecknobit.gluky.requester
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.analyses.data.Report
import com.tecknobit.gluky.ui.screens.analyses.helpers.openReportLabel
import com.tecknobit.gluky.ui.screens.shared.presentations.ToastsLauncher
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.isCustomTrendPeriodValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.failed_to_download_report
import gluky.composeapp.generated.resources.report_created
import gluky.composeapp.generated.resources.wrong_custom_range
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), ToastsLauncher {

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    override lateinit var toasterState: ToasterState

    override var scope: CoroutineScope = viewModelScope

    private val _glycemicTrend = MutableStateFlow<GlycemicTrendDataContainer?>(
        value = null
    )
    val glycemicTrend = _glycemicTrend.asStateFlow()

    private val _glycemicTrendPeriod = MutableStateFlow(
        value = ONE_MONTH
    )
    val glycemicTrendPeriod = _glycemicTrendPeriod.asStateFlow()

    private val _glycemicTrendGroupingDay = MutableStateFlow<GlycemicTrendGroupingDay?>(
        value = null
    )
    val glycemicTrendGroupingDay = _glycemicTrendGroupingDay.asStateFlow()

    private val _creatingReport = MutableStateFlow(
        value = false
    )
    val creatingReport = _creatingReport.asStateFlow()

    lateinit var customPeriodPickerState: DateRangePickerState

    fun retrieveGlycemicTrend(
        from: Long? = null,
        to: Long? = null,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getGlycemicTrend(
                        period = _glycemicTrendPeriod.value,
                        groupingDay = _glycemicTrendGroupingDay.value,
                        from = from,
                        to = to
                    )
                },
                onSuccess = {
                    sessionFlowState.notifyOperational()
                    _glycemicTrend.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = {
                    sessionFlowState.notifyUserDisconnected()
                },
                onConnectionError = {
                    sessionFlowState.notifyServerOffline()
                }
            )
        }
    }

    fun selectGlycemicGroupingDay(
        groupingDay: GlycemicTrendGroupingDay,
    ) {
        _glycemicTrendGroupingDay.value = groupingDay
        retrieveGlycemicTrend()
    }

    fun selectGlycemicTrendPeriod(
        period: GlycemicTrendPeriod,
    ) {
        _glycemicTrendPeriod.value = period
        retrieveGlycemicTrend()
    }

    fun applyCustomTrendPeriod(
        allowedPeriod: String,
        onApply: () -> Unit,
    ) {
        if (!isCustomPeriodValid(customPeriodPickerState)) {
            toastError(
                error = Res.string.wrong_custom_range,
                allowedPeriod
            )
            return
        }
        retrieveGlycemicTrend(
            from = customPeriodPickerState.selectedStartDateMillis,
            to = customPeriodPickerState.selectedEndDateMillis
        )
        onApply()
    }

    @Wrapper
    @Validator
    private fun isCustomPeriodValid(
        state: DateRangePickerState,
    ): Boolean {
        return isCustomTrendPeriodValid(
            from = state.selectedStartDateMillis,
            to = state.selectedEndDateMillis,
            period = _glycemicTrendPeriod.value
        )
    }

    fun createReport(
        onCreated: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    _creatingReport.value = true
                    createReport(
                        period = _glycemicTrendPeriod.value,
                        groupingDay = _glycemicTrendGroupingDay.value,
                        from = customPeriodPickerState.selectedStartDateMillis,
                        to = customPeriodPickerState.selectedEndDateMillis
                    )
                },
                onSuccess = {
                    val report: Report = Json.decodeFromJsonElement(it.toResponseData())
                    downloadReport(
                        report = report,
                        onDownloadCompleted = onCreated
                    )
                },
                onFailure = {
                    _creatingReport.value = false
                    onCreated()
                    showSnackbarMessage(it)
                }
            )
        }
    }

    private fun downloadReport(
        report: Report,
        onDownloadCompleted: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                requester.downloadReport(
                    report = report,
                    onDownloadCompleted = { reportUrl ->
                        _creatingReport.value = false
                        onDownloadCompleted()
                        deleteReport(
                            report = report
                        )
                        completedSnackMessage(
                            reportUrl = reportUrl
                        )
                    }
                )
            } catch (e: IllegalStateException) {
                showSnackbarMessage(
                    message = Res.string.failed_to_download_report
                )
            }
        }
    }

    private fun deleteReport(
        report: Report,
    ) {
        val scope = CoroutineScope(
            context = Dispatchers.Default
        )
        scope.launch {
            requester.deleteReport(
                report = report
            )
        }
    }

    private fun completedSnackMessage(
        reportUrl: String?,
    ) {
        showSnackbarMessage(
            message = Res.string.report_created,
            actionLabel = openReportLabel,
            onActionPerformed = {
                val kReviewer = KReviewer()
                kReviewer.reviewInApp {
                    openReport(
                        url = reportUrl
                    )
                }
            }
        )
    }

}
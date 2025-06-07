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

/**
 * The `AnalysesScreenViewModel` class is the support class used to manage the analyses requests
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see ToastsLauncher
 */
class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), ToastsLauncher {

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    /**
     * `toasterState` the state used to launch the toasts messages
     */
    override lateinit var toasterState: ToasterState

    /**
     * `toasterState` the state used to launch the toasts messages
     */
    override var scope: CoroutineScope = viewModelScope

    /**
     * `_glycemicTrend` the glycemic trend data
     */
    private val _glycemicTrend = MutableStateFlow<GlycemicTrendDataContainer?>(
        value = null
    )
    val glycemicTrend = _glycemicTrend.asStateFlow()

    /**
     * `_glycemicTrendPeriod` the current selected trend period
     */
    private val _glycemicTrendPeriod = MutableStateFlow(
        value = ONE_MONTH
    )
    val glycemicTrendPeriod = _glycemicTrendPeriod.asStateFlow()

    /**
     * `_glycemicTrendGroupingDay` the current selected grouping day
     */
    private val _glycemicTrendGroupingDay = MutableStateFlow<GlycemicTrendGroupingDay?>(
        value = null
    )
    val glycemicTrendGroupingDay = _glycemicTrendGroupingDay.asStateFlow()

    /**
     * `_creatingReport` whether a report is currently in creation
     */
    private val _creatingReport = MutableStateFlow(
        value = false
    )
    val creatingReport = _creatingReport.asStateFlow()

    /**
     * `customPeriodPickerState` the state of the picker used to select a custom dates range
     */
    lateinit var customPeriodPickerState: DateRangePickerState

    /**
     * Method used to request the glycemic trend
     *
     * @param from        The start date from retrieve the measurements
     * @param to          The end date to retrieve the measurements
     */
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

    /**
     * Method used to apply the new value of the grouping day
     *
     * @param groupingDay The selected grouping day
     */
    fun selectGlycemicGroupingDay(
        groupingDay: GlycemicTrendGroupingDay,
    ) {
        _glycemicTrendGroupingDay.value = groupingDay
        retrieveGlycemicTrend()
    }

    /**
     * Method used to apply the new value of the trend period
     *
     * @param period The selected trend period period
     */
    fun selectGlycemicTrendPeriod(
        period: GlycemicTrendPeriod,
    ) {
        _glycemicTrendPeriod.value = period
        retrieveGlycemicTrend()
    }

    /**
     * Method used to select the selected custom range dates
     *
     * @param allowedPeriod The maximum allowed period
     * @param onApply The callback to invoke when the user selected the custom range
     */
    fun applyCustomTrendPeriod(
        allowedPeriod: String,
        onApply: () -> Unit,
    ) {
        if (!isCustomPeriodValid()) {
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

    /**
     * Method used to validate the selected custom period
     *
     * @return whether the selected custom period is valid as [Boolean]
     */
    @Validator
    private fun isCustomPeriodValid(): Boolean {
        return isCustomTrendPeriodValid(
            from = customPeriodPickerState.selectedStartDateMillis,
            to = customPeriodPickerState.selectedEndDateMillis,
            period = _glycemicTrendPeriod.value
        )
    }

    /**
     * Method used to request to create a report
     *
     * @param onCreated The callback to invoke after the report created
     */
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

    /**
     * Method used to download the created report
     *
     * @param report The report to download
     * @param onDownloadCompleted The callback to invoke after the report downloaded
     */
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

    /**
     * Method used to show the snackbar about the download completion
     *
     * @param reportUrl The url where the report has been saved
     */
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

    /**
     * Method used to request the report deletion
     *
     * @param report The report to delete
     */
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

}
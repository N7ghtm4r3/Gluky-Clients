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
import com.tecknobit.gluky.requester
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.shared.presentations.ToastsLauncher
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.isCustomTrendPeriodValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.open
import gluky.composeapp.generated.resources.report_created
import gluky.composeapp.generated.resources.wrong_custom_range
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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
        rangePickerState: DateRangePickerState,
        allowedPeriod: String,
        onApply: () -> Unit,
    ) {
        if (!isCustomPeriodValid(rangePickerState)) {
            toastError(
                error = Res.string.wrong_custom_range,
                allowedPeriod
            )
            return
        }
        retrieveGlycemicTrend(
            from = rangePickerState.selectedStartDateMillis,
            to = rangePickerState.selectedEndDateMillis
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
        // TODO: TO MAKE THE REQUEST THEN DOWNLOAD FROM THE URL RETURNED
        _creatingReport.value = true
        viewModelScope.launch {
            delay(2000L) // TODO: TO REMOVE
            _creatingReport.value = false
            onCreated()
            showSnackbarMessage(
                message = Res.string.report_created,
                actionLabel = Res.string.open,
                onActionPerformed = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        // TODO: OPEN THE FILE
                    }
                }
            )
        }
    }

}
package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.ALL
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_WEEK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    private val _glycemicTrendData = MutableStateFlow<GlycemicTrendData?>(
        value = null
    )
    val glycemicTrendData = _glycemicTrendData.asStateFlow()

    private val _glycemicTrendPeriod = MutableStateFlow(
        value = ONE_WEEK
    )
    val glycemicTrendPeriod = _glycemicTrendPeriod.asStateFlow()

    private val _glycemicTrendGroupingDay = MutableStateFlow(
        value = ALL
    )
    val glycemicTrendGroupingDay = _glycemicTrendGroupingDay.asStateFlow()

    fun retrieveGlycemiaTrendData() {
        // TODO: TO MAKE THE REQUEST THEN
        viewModelScope.launch {
            delay(2000)
            _glycemicTrendData.value = GlycemicTrendData()
        }
    }

    fun selectGlycemicTrendPeriod(
        period: GlycemicTrendPeriod,
    ) {
        _glycemicTrendPeriod.value = period
    }

}
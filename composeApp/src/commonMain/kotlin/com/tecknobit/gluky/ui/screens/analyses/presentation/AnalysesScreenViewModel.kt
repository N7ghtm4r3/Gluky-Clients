package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    private val _glycemiaTrendData = MutableStateFlow<GlycemiaTrendData?>(
        value = null
    )
    val glycemiaTrendData = _glycemiaTrendData.asStateFlow()

    fun retrieveGlycemiaTrendData() {
        // TODO: TO MAKE THE REQUEST THEN
        viewModelScope.launch {
            delay(2000)
            _glycemiaTrendData.value = GlycemiaTrendData()
        }
    }

}
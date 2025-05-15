package com.tecknobit.gluky.ui.screens.meals.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private companion object {

        const val ONE_DAY_MILLIS = 86_400_000

    }

    private val _day = MutableStateFlow(
        value = currentTimestamp()
    )
    val day = _day.asStateFlow()

    private var previousPage = 50

    fun computeDayValue(
        page: Int,
    ) {
        val offset = page - previousPage
        _day.value += (ONE_DAY_MILLIS * offset)
        previousPage = page
    }

}
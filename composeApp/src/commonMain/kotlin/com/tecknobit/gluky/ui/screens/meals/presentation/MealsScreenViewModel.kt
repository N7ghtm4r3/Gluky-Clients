package com.tecknobit.gluky.ui.screens.meals.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        const val ONE_DAY_MILLIS = 86_400_000L

        const val MAX_LOADABLE_DAYS = 100

        const val INITIAL_SELECTED_DAY = MAX_LOADABLE_DAYS / 2

    }

    private val _currentDay = MutableStateFlow(
        value = currentTimestamp()
    )
    val currentDay = _currentDay.asStateFlow()

    private var previousPage = INITIAL_SELECTED_DAY

    fun computeDayValue(
        page: Int,
    ) {
        val offset = page - previousPage
        _currentDay.value += (ONE_DAY_MILLIS * offset)
        previousPage = page
    }

    fun setCurrentDay(
        millis: Long,
    ) {
        _currentDay.value = millis
    }

}
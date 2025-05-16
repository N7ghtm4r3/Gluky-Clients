package com.tecknobit.gluky.ui.screens.meals.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MealsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        const val ONE_DAY_MILLIS = 86_400_000L

        const val MAX_LOADABLE_DAYS = 100

        const val INITIAL_SELECTED_DAY = MAX_LOADABLE_DAYS / 2

    }

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    private val _currentDay = MutableStateFlow(
        value = currentTimestamp()
    )
    val currentDay = _currentDay.asStateFlow()

    private var previousPage = INITIAL_SELECTED_DAY

    private val _mealDay = MutableStateFlow<MealDayData?>(
        value = null
    )
    val mealDay = _mealDay.asStateFlow()

    fun computeDayValue(
        page: Int,
    ) {
        val offset = page - previousPage
        _currentDay.value += (ONE_DAY_MILLIS * offset)
        previousPage = page
        retrieveMealDay()
    }

    fun setCurrentDay(
        millis: Long,
    ) {
        _currentDay.value = millis
        retrieveMealDay()
    }

    fun retrieveMealDay() {
        // TODO: TO MAKE THE REQUEST THEN
        viewModelScope.launch {
            _mealDay.value = MealDayData(
                id = Random.nextLong().toString(),
                breakfast = Meal(
                    id = Random.nextLong().toString(),
                    type = BREAKFAST,
                    annotationDate = currentTimestamp(),
                    content = "",
                    glycemia = 80,
                    insulinUnits = 2
                ),
                morningSnack = Meal(
                    id = Random.nextLong().toString(),
                    type = MORNING_SNACK,
                    annotationDate = currentTimestamp(),
                    content = "",
                    glycemia = 80,
                    insulinUnits = 2
                ),
                lunch = Meal(
                    id = Random.nextLong().toString(),
                    type = LUNCH,
                    annotationDate = currentTimestamp(),
                    content = "",
                    glycemia = 80,
                    insulinUnits = 2
                ),
                afternoonSnack = Meal(
                    id = Random.nextLong().toString(),
                    type = AFTERNOON_SNACK,
                    annotationDate = currentTimestamp(),
                    content = "",
                    glycemia = 80,
                    insulinUnits = 2
                ),
                dinner = Meal(
                    id = Random.nextLong().toString(),
                    type = DINNER,
                    annotationDate = currentTimestamp(),
                    content = "",
                    glycemia = 80,
                    insulinUnits = 2
                ),
            )
        }
    }

}
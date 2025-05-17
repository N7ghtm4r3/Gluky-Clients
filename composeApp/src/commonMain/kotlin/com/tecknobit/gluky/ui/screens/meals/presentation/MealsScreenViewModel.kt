package com.tecknobit.gluky.ui.screens.meals.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
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

    lateinit var glycemia: MutableState<String>

    lateinit var glycemiaError: MutableState<Boolean>

    lateinit var postPrandialGlycemia: MutableState<String>

    lateinit var postPrandialGlycemiaError: MutableState<Boolean>

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
                    content = "- Latte parzialmente scremato (200 ml)\n" +
                            "- Fette biscottate integrali (4 unità)\n" +
                            "- Marmellata senza zuccheri aggiunti (20 g)",
                    glycemia = Random.nextInt(200),
                    postPrandialGlycemia = Random.nextInt(500),
                    insulinUnits = Random.nextInt(20)
                ),
                morningSnack = Meal(
                    id = Random.nextLong().toString(),
                    type = MORNING_SNACK,
                    annotationDate = currentTimestamp(),
                    content = """
                    - Yogurt greco 0% (150 g)
                    - Mandorle (10 g)
                    """.trimIndent(),
                    glycemia = Random.nextInt(200),
                    postPrandialGlycemia = Random.nextInt(200)
                ),
                lunch = Meal(
                    id = Random.nextLong().toString(),
                    type = LUNCH,
                    annotationDate = currentTimestamp(),
                    content = """
                    - Riso integrale (80 g)
                    - Petto di pollo alla griglia (150 g)
                    - Zucchine grigliate (200 g)
                    - Olio extravergine di oliva (10 g)
                    """.trimIndent(),
                    glycemia = Random.nextInt(200),
                    postPrandialGlycemia = Random.nextInt(200),
                    insulinUnits = Random.nextInt(20)
                ),
                afternoonSnack = Meal(
                    id = Random.nextLong().toString(),
                    type = AFTERNOON_SNACK,
                    annotationDate = currentTimestamp(),
                    content = """
                    - Frutta fresca (mela) (150 g)
                    - Noci (3 unità)
                    """.trimIndent(),
                    glycemia = Random.nextInt(200),
                    postPrandialGlycemia = Random.nextInt(200)
                ),
                dinner = Meal(
                    id = Random.nextLong().toString(),
                    type = DINNER,
                    annotationDate = currentTimestamp(),
                    content = """
                    - Filetto di salmone al forno (150 g)
                    - Patate lesse (200 g)
                    - Insalata mista (100 g)
                    - Olio extravergine di oliva (10 g)
                    """.trimIndent(),
                    glycemia = Random.nextInt(200),
                    postPrandialGlycemia = -1,
                    insulinUnits = Random.nextInt(20)
                ),
            )
        }
    }

    fun fillMeal(
        meal: Meal,
        onSave: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSave()
    }

}
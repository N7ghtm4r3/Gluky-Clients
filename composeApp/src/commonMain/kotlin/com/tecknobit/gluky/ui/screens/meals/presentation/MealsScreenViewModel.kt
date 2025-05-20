package com.tecknobit.gluky.ui.screens.meals.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.tecknobit.equinoxcompose.components.quantitypicker.QuantityPickerState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import com.tecknobit.gluky.ui.screens.meals.data.BasalInsulin
import com.tecknobit.gluky.ui.screens.meals.data.Meal
import com.tecknobit.gluky.ui.screens.meals.data.MealDayData
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.glycemiaValueIsValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.invalid_meal_content
import gluky.composeapp.generated.resources.wrong_glycemia_value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
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

    lateinit var insulinUnits: QuantityPickerState

    lateinit var insulinNeeded: MutableState<Boolean>

    lateinit var mealContent: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    lateinit var toaster: ToasterState

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
            _mealDay.value = if (false) {
                MealDayData(
                    id = Random.nextLong().toString(),
                    breakfast = Meal(
                        id = Random.nextLong().toString(),
                        type = BREAKFAST
                    ),
                    morningSnack = Meal(
                        id = Random.nextLong().toString(),
                        type = MORNING_SNACK
                    ),
                    lunch = Meal(
                        id = Random.nextLong().toString(),
                        type = LUNCH
                    ),
                    afternoonSnack = Meal(
                        id = Random.nextLong().toString(),
                        type = AFTERNOON_SNACK
                    ),
                    dinner = Meal(
                        id = Random.nextLong().toString(),
                        type = DINNER
                    ),
                    basalInsulin = BasalInsulin(
                        id = Random.nextLong().toString()
                    )
                )
            } else
                null
        }
    }

    fun fillDay() {
        // TODO: TO MAKE THE REQUEST THEN
        val obtainedByTheRequest = MealDayData(
            id = Random.nextLong().toString(),
            breakfast = Meal(
                id = Random.nextLong().toString(),
                type = BREAKFAST
            ),
            morningSnack = Meal(
                id = Random.nextLong().toString(),
                type = MORNING_SNACK
            ),
            lunch = Meal(
                id = Random.nextLong().toString(),
                type = LUNCH
            ),
            afternoonSnack = Meal(
                id = Random.nextLong().toString(),
                type = AFTERNOON_SNACK
            ),
            dinner = Meal(
                id = Random.nextLong().toString(),
                type = DINNER
            ),
            basalInsulin = BasalInsulin(
                id = Random.nextLong().toString()
            )
        ) // TODO: TO USE THE REAL ONE INSTEAD
        _mealDay.value = obtainedByTheRequest
    }

    fun fillMeal(
        meal: Meal,
        onSave: () -> Unit,
    ) {
        if (!areMealDataValid())
            return
        // TODO: MAKE THE REQUEST THEN -> MUST RETURN THE NEW RAWCONTENT AND THE CONTENT TO LOCALLY ASSIGN
        locallyUpdateMeal(
            meal = meal,
            rawContent = buildJsonObject { }, // TODO: TO RETRIEVE FROM RESPONSE
            content = "- Filetto di salmone al forno (${Random.nextInt(1000)}g)" // TODO: TO RETRIEVE FROM RESPONSE
        )
        onSave()
    }

    private fun areMealDataValid(): Boolean {
        if (!glycemiaValueIsValid(glycemia.value)) {
            toastGlycemiaValueError()
            glycemiaError.value = true
            return false
        }
        if (!glycemiaValueIsValid(postPrandialGlycemia.value)) {
            toastGlycemiaValueError()
            postPrandialGlycemiaError.value = true
            return false
        }
        mealContent.forEach { entry ->
            if (entry.first.value.isEmpty() || entry.second.value.isEmpty()) {
                toastError(
                    error = Res.string.invalid_meal_content
                )
                return false
            }
        }
        return true
    }

    private fun locallyUpdateMeal(
        meal: Meal,
        rawContent: JsonObject,
        content: String,
    ) {
        meal.glycemia.value = glycemia.toNormalizedGlycemicValue()
        meal.postPrandialGlycemia.value = postPrandialGlycemia.toNormalizedGlycemicValue()
        meal.insulinUnits.value = if (insulinNeeded.value)
            insulinUnits.quantityPicked
        else
            -1
        meal.rawContent.value = rawContent
        meal.content.value = content
    }

    private fun MutableState<String>.toNormalizedGlycemicValue(): Int {
        return if (value.isEmpty())
            -1
        else
            value.toInt()
    }

    @Wrapper
    private fun toastGlycemiaValueError() {
        toastError(
            error = Res.string.wrong_glycemia_value
        )
    }

    private fun toastError(
        error: StringResource,
    ) {
        viewModelScope.launch {
            val errorMessage = getString(
                resource = error
            )
            toaster.show(
                toast = Toast(
                    message = errorMessage,
                    type = ToastType.Error
                )
            )
        }
    }

}
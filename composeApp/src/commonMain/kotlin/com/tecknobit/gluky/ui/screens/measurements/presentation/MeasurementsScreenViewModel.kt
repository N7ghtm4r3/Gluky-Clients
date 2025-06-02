package com.tecknobit.gluky.ui.screens.measurements.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToasterState
import com.tecknobit.equinoxcompose.components.quantitypicker.QuantityPickerState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.Requester.Companion.toNullableResponseData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import com.tecknobit.gluky.helpers.KReviewer
import com.tecknobit.gluky.requester
import com.tecknobit.gluky.ui.screens.measurements.data.BasalInsulin
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.data.GlycemicMeasurementItem
import com.tecknobit.gluky.ui.screens.measurements.data.Meal
import com.tecknobit.gluky.ui.screens.shared.presentations.ToastsLauncher
import com.tecknobit.glukycore.helpers.GlukyInputsValidator.isGlycemiaValueValid
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.invalid_meal_content
import gluky.composeapp.generated.resources.wrong_glycemia_value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class MeasurementsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), ToastsLauncher {

    companion object {

        const val ONE_DAY_MILLIS = 86_400_000L

        const val MAX_LOADABLE_DAYS = 100

        const val INITIAL_SELECTED_DAY = MAX_LOADABLE_DAYS / 2

    }

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    override lateinit var toasterState: ToasterState

    override var scope: CoroutineScope = viewModelScope

    private val _currentDay = MutableStateFlow(
        value = currentTimestamp()
    )
    val currentDay = _currentDay.asStateFlow()

    private var previousPage = INITIAL_SELECTED_DAY

    private val _dailyMeasurements = MutableStateFlow<DailyMeasurements?>(
        value = null
    )
    val dailyMeasurements = _dailyMeasurements.asStateFlow()

    lateinit var glycemia: MutableState<String>

    lateinit var glycemiaError: MutableState<Boolean>

    lateinit var postPrandialGlycemia: MutableState<String>

    lateinit var postPrandialGlycemiaError: MutableState<Boolean>

    lateinit var insulinUnits: QuantityPickerState

    lateinit var insulinNeeded: MutableState<Boolean>

    lateinit var mealContent: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    fun computeDayValue(
        page: Int,
    ) {
        val offset = page - previousPage
        _currentDay.value += (ONE_DAY_MILLIS * offset)
        previousPage = page
        retrieveDailyMeasurements()
    }

    fun setCurrentDay(
        millis: Long,
    ) {
        _currentDay.value = millis
        retrieveDailyMeasurements()
    }

    @OptIn(ExperimentalComposeApi::class)
    fun retrieveDailyMeasurements() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getDailyMeasurements(
                        targetDay = _currentDay.value
                    )
                },
                onSuccess = {
                    sessionFlowState.notifyOperational()
                    val jDailyMeasurements = it.toNullableResponseData()
                    if (jDailyMeasurements == null)
                        _dailyMeasurements.value = null
                    else
                        _dailyMeasurements.value = Json.decodeFromJsonElement(jDailyMeasurements)
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

    fun fillDay() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    fillDay(
                        targetDay = _currentDay.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        _dailyMeasurements.value = Json.decodeFromJsonElement(it.toResponseData())
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun fillMeal(
        meal: Meal,
        onSave: () -> Unit,
    ) {
        if (!areMealDataValid())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    fillMeal(
                        targetDay = _currentDay.value,
                        mealType = meal.type,
                        glycemia = glycemia.value,
                        postPrandialGlycemia = postPrandialGlycemia.value,
                        insulinUnits = insulinUnits.quantityPicked,
                        content = mealContent
                    )
                },
                onSuccess = {
                    val rawContent: JsonObject = Json.decodeFromJsonElement(it.toResponseData())
                    locallyUpdateMeal(
                        meal = meal,
                        rawContent = rawContent,
                    )
                    onSave()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @Validator
    private fun areMealDataValid(): Boolean {
        if (!isGlycemicValueValid())
            return false
        if (!isGlycemiaValueValid(postPrandialGlycemia.value)) {
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
    ) {
        locallyUpdateGlycemicMeasurementItem(
            item = meal
        )
        meal.postPrandialGlycemia.value = postPrandialGlycemia.toNormalizedGlycemicValue()
        meal.rawContent.value = rawContent
    }

    fun fillBasalInsulin(
        basalInsulin: BasalInsulin,
        onSave: () -> Unit,
    ) {
        if (!areBasalInsulinDataValid())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    fillBasalInsulin(
                        targetDay = _currentDay.value,
                        glycemia = glycemia.value,
                        insulinUnits = insulinUnits.quantityPicked,
                    )
                },
                onSuccess = {
                    locallyUpdateBasalInsulin(
                        basalInsulin = basalInsulin
                    )
                    onSave()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @Wrapper
    @Validator
    private fun areBasalInsulinDataValid(): Boolean {
        return isGlycemicValueValid()
    }

    @Validator
    private fun isGlycemicValueValid(): Boolean {
        return if (!isGlycemiaValueValid(glycemia.value)) {
            toastGlycemiaValueError()
            glycemiaError.value = true
            false
        } else
            true
    }

    @Wrapper
    private fun locallyUpdateBasalInsulin(
        basalInsulin: BasalInsulin,
    ) {
        locallyUpdateGlycemicMeasurementItem(
            item = basalInsulin
        )
    }

    private fun locallyUpdateGlycemicMeasurementItem(
        item: GlycemicMeasurementItem,
    ) {
        if (item.annotationDate.value == -1L)
            item.annotationDate.value = currentTimestamp()
        item.glycemia.value = glycemia.toNormalizedGlycemicValue()
        item.insulinUnits.value = if (insulinNeeded.value)
            insulinUnits.quantityPicked
        else
            -1
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

    fun saveDailyNotes(
        content: String,
        onSave: () -> Unit,
    ) {
        // TODO: TO MAKE THE REQUEST THEN
        _dailyMeasurements.value!!.dailyNotes = content
        onSave()
    }

}
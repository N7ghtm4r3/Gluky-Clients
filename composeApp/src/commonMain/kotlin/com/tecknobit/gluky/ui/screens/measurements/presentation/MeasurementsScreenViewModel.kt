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
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.Requester.Companion.toNullableResponseData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.time.TimeFormatter.currentTimestamp
import com.tecknobit.gluky.helpers.KReviewer
import com.tecknobit.gluky.requester
import com.tecknobit.gluky.ui.screens.measurements.data.DailyMeasurements
import com.tecknobit.gluky.ui.screens.measurements.data.types.BasalInsulin
import com.tecknobit.gluky.ui.screens.measurements.data.types.GlycemicMeasurementItem
import com.tecknobit.gluky.ui.screens.measurements.data.types.Meal
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

/**
 * The `MeasurementsScreenViewModel` class is the support class used to manage the daily measurements
 * requests
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see ToastsLauncher
 */
class MeasurementsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), ToastsLauncher {

    companion object {

        /**
         * `ONE_DAY_MILLIS` constant value representing one day in millis
         */
        const val ONE_DAY_MILLIS = 86_400_000L

        /**
         * `MAX_LOADABLE_DAYS` the maximum loadable days
         */
        const val MAX_LOADABLE_DAYS = 100

        /**
         * `INITIAL_SELECTED_DAY` the initial selected day, so the middle of the [MAX_LOADABLE_DAYS]
         * range
         */
        const val INITIAL_SELECTED_DAY = MAX_LOADABLE_DAYS / 2

    }

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
     * `_currentDay` the current day selected
     */
    private val _currentDay = MutableStateFlow(
        value = currentTimestamp()
    )
    val currentDay = _currentDay.asStateFlow()

    /**
     * `previousPage` the previous page displayed for the previous day
     */
    private var previousPage = INITIAL_SELECTED_DAY

    /**
     * `_dailyMeasurements` the daily measurements related to the selected [_currentDay]
     */
    private val _dailyMeasurements = MutableStateFlow<DailyMeasurements?>(
        value = null
    )
    val dailyMeasurements = _dailyMeasurements.asStateFlow()

    /**
     * `glycemia` the value of the glycemia
     */
    lateinit var glycemia: MutableState<String>

    /**
     * `glycemiaError` whether the [glycemia] field is not valid
     */
    lateinit var glycemiaError: MutableState<Boolean>

    /**
     * `postPrandialGlycemia` the value of the post-prandial glycemia
     */
    lateinit var postPrandialGlycemia: MutableState<String>

    /**
     * `postPrandialGlycemiaError` whether the [postPrandialGlycemia] field is not valid
     */
    lateinit var postPrandialGlycemiaError: MutableState<Boolean>

    /**
     * `insulinUnits` the state used to pick the insulin units quantity
     */
    lateinit var insulinUnits: QuantityPickerState

    /**
     * `insulinNeeded` whether the measurement requires the insulin units administration
     */
    lateinit var insulinNeeded: MutableState<Boolean>

    /**
     * `mealContent` list with the content of a meal
     */
    lateinit var mealContent: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    /**
     * Method used to compute the value in millis of the [_currentDay] based on the [page]
     *
     * @param page The selected page
     */
    fun computeDayValue(
        page: Int,
    ) {
        val offset = page - previousPage
        _currentDay.value += (ONE_DAY_MILLIS * offset)
        previousPage = page
        retrieveDailyMeasurements()
    }

    /**
     * Method used to set the value for the [_currentDay]
     *
     * @param millis The millis value to set
     */
    fun setCurrentDay(
        millis: Long,
    ) {
        _currentDay.value = millis
        retrieveDailyMeasurements()
    }

    /**
     * Method used to request to retrieve the daily measurements related to the selected [_currentDay]
     */
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

    /**
     * Method used to request the filling of the selected [_currentDay]
     */
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

    /**
     * Method used to request to fill a meal
     *
     * @param meal The meal to fill
     * @param onSave The callback to invoke after the meal has been filled
     */
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

    /**
     * Method used to validate the meal data before request its filling
     *
     * @return whether the meal data are valid as [Boolean]
     */
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

    /**
     * Method used to locally update the [meal] with the new values inserted
     *
     * @param meal The meal to update
     * @param rawContent The raw content returned by the request
     */
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

    /**
     * Method used to request to fill a basal insulin record
     *
     * @param basalInsulin The basal insulin to fill
     * @param onSave The callback to invoke after the meal has been filled
     */
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

    /**
     * Method used to validate the basal insulin data before request its filling
     *
     * @return whether the basal insulin data are valid as [Boolean]
     */
    @Wrapper
    @Validator
    private fun areBasalInsulinDataValid(): Boolean {
        return isGlycemicValueValid()
    }

    /**
     * Method used to validate a glycemic value
     *
     * @return whether the glycemic value is valid as [Boolean]
     */
    @Validator
    private fun isGlycemicValueValid(): Boolean {
        return if (!isGlycemiaValueValid(glycemia.value)) {
            toastGlycemiaValueError()
            glycemiaError.value = true
            false
        } else
            true
    }

    /**
     * Method used to locally update the [basalInsulin] with the new values inserted
     *
     * @param basalInsulin The basal insulin record to update
     */
    @Wrapper
    private fun locallyUpdateBasalInsulin(
        basalInsulin: BasalInsulin,
    ) {
        locallyUpdateGlycemicMeasurementItem(
            item = basalInsulin
        )
    }

    /**
     * Method used to locally update the [item] with the new values inserted
     *
     * @param item The item to update
     */
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

    /**
     * Method used to normalize a string value into a integer glycemic value
     *
     * @return the normalized glycemic value as [Int]
     */
    @Returner
    private fun MutableState<String>.toNormalizedGlycemicValue(): Int {
        return if (value.isEmpty())
            -1
        else
            value.toInt()
    }

    /**
     * Method used to toast an error message related to a wrong glycemic value
     */
    @Wrapper
    private fun toastGlycemiaValueError() {
        toastError(
            error = Res.string.wrong_glycemia_value
        )
    }

    /**
     * Method used to request to save the daily notes content
     *
     * @param content The content of the daily notes
     * @param onSave The callback to invoke after the notes have been saved
     */
    fun saveDailyNotes(
        content: String,
        onSave: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    saveDailyNotes(
                        targetDay = _currentDay.value,
                        content = content
                    )
                },
                onSuccess = {
                    _dailyMeasurements.value!!.dailyNotes = content
                    onSave()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}
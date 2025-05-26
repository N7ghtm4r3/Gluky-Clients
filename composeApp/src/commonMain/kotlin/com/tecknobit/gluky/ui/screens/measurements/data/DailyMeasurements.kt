package com.tecknobit.gluky.ui.screens.measurements.data

import com.tecknobit.glukycore.AFTERNOON_SNACK_KEY
import com.tecknobit.glukycore.BASAL_INSULIN_KEY
import com.tecknobit.glukycore.DAILY_NOTES_KEY
import com.tecknobit.glukycore.MORNING_SNACK_KEY
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyMeasurements(
    val id: String,
    val breakfast: Meal,
    @SerialName(MORNING_SNACK_KEY)
    val morningSnack: Meal,
    val lunch: Meal,
    @SerialName(AFTERNOON_SNACK_KEY)
    val afternoonSnack: Meal,
    val dinner: Meal,
    @SerialName(BASAL_INSULIN_KEY)
    val basalInsulin: BasalInsulin,
    @SerialName(DAILY_NOTES_KEY)
    var dailyNotes: String = "",
) {

    fun getMealByType(
        type: MeasurementType,
    ): Meal {
        require(type != BASAL_INSULIN)
        return when (type) {
            BREAKFAST -> breakfast
            MORNING_SNACK -> morningSnack
            LUNCH -> lunch
            AFTERNOON_SNACK -> afternoonSnack
            else -> dinner
        }
    }

}
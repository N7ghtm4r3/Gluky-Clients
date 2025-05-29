package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.glukycore.AFTERNOON_SNACK_KEY
import com.tecknobit.glukycore.AVERAGE_GLYCEMIA_KEY
import com.tecknobit.glukycore.BASAL_INSULIN_KEY
import com.tecknobit.glukycore.FIRST_SET_KEY
import com.tecknobit.glukycore.FOURTH_SET_KEY
import com.tecknobit.glukycore.GLYCEMIC_LABEL_TYPE_KEY
import com.tecknobit.glukycore.HIGHER_GLYCEMIA_KEY
import com.tecknobit.glukycore.LOWER_GLYCEMIA_KEY
import com.tecknobit.glukycore.MORNING_SNACK_KEY
import com.tecknobit.glukycore.SECOND_SET_KEY
import com.tecknobit.glukycore.THIRD_SET_KEY
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.enums.MeasurementType.AFTERNOON_SNACK
import com.tecknobit.glukycore.enums.MeasurementType.BASAL_INSULIN
import com.tecknobit.glukycore.enums.MeasurementType.BREAKFAST
import com.tecknobit.glukycore.enums.MeasurementType.DINNER
import com.tecknobit.glukycore.enums.MeasurementType.LUNCH
import com.tecknobit.glukycore.enums.MeasurementType.MORNING_SNACK
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlycemicTrendDataContainer(
    val breakfast: GlycemicTrendData? = null,
    @SerialName(MORNING_SNACK_KEY)
    val morningSnack: GlycemicTrendData? = null,
    val lunch: GlycemicTrendData? = null,
    @SerialName(AFTERNOON_SNACK_KEY)
    val afternoonSnack: GlycemicTrendData? = null,
    val dinner: GlycemicTrendData? = null,
    @SerialName(BASAL_INSULIN_KEY)
    val basalInsulin: GlycemicTrendData? = null,
) {

    val availableSets: MutableList<MeasurementType> = mutableListOf()

    init {
        breakfast.ifIsNotNullAppend(
            type = BREAKFAST
        )
        morningSnack.ifIsNotNullAppend(
            type = MORNING_SNACK
        )
        lunch.ifIsNotNullAppend(
            type = LUNCH
        )
        afternoonSnack.ifIsNotNullAppend(
            type = AFTERNOON_SNACK
        )
        dinner.ifIsNotNullAppend(
            type = DINNER
        )
        basalInsulin.ifIsNotNullAppend(
            type = BASAL_INSULIN
        )
    }

    fun dataAvailable(): Boolean {
        return breakfast != null ||
                morningSnack != null ||
                lunch != null ||
                afternoonSnack != null ||
                dinner != null
    }

    private fun GlycemicTrendData?.ifIsNotNullAppend(
        type: MeasurementType,
    ) {
        if (this == null)
            return
        availableSets.add(type)
    }

    fun getRelatedSet(
        type: MeasurementType,
    ): GlycemicTrendData? {
        return when (type) {
            BREAKFAST -> breakfast
            MORNING_SNACK -> morningSnack
            LUNCH -> lunch
            AFTERNOON_SNACK -> afternoonSnack
            DINNER -> dinner
            BASAL_INSULIN -> basalInsulin
        }
    }

    fun firstAvailableDate(): Long? {
        return breakfast?.firstSet?.first()?.date
    }

    fun lastAvailableDate(): Long? {
        return breakfast?.firstSet?.last()?.date
    }

}

@Serializable
data class GlycemicTrendData(
    @SerialName(HIGHER_GLYCEMIA_KEY)
    val higherGlycemia: GlycemiaPoint,
    @SerialName(LOWER_GLYCEMIA_KEY)
    val lowerGlycemia: GlycemiaPoint,
    @SerialName(AVERAGE_GLYCEMIA_KEY)
    val averageGlycemia: GlycemiaPoint,
    @SerialName(FIRST_SET_KEY)
    val firstSet: List<GlycemiaPoint>? = null,
    @SerialName(SECOND_SET_KEY)
    val secondSet: List<GlycemiaPoint>? = null,
    @SerialName(THIRD_SET_KEY)
    val thirdSet: List<GlycemiaPoint>? = null,
    @SerialName(FOURTH_SET_KEY)
    val fourthSet: List<GlycemiaPoint>? = null,
    @SerialName(GLYCEMIC_LABEL_TYPE_KEY)
    val labelType: GlycemicTrendLabelType? = null,
) {

    val sets: MutableList<List<GlycemiaPoint>> = mutableListOf()

    init {
        firstSet.ifIsNotNullAppend()
        secondSet.ifIsNotNullAppend()
        thirdSet.ifIsNotNullAppend()
        fourthSet.ifIsNotNullAppend()
    }

    fun getSpecifiedSet(
        index: Int,
    ): List<GlycemiaPoint>? {
        return when (index) {
            0 -> firstSet
            1 -> secondSet
            2 -> thirdSet
            3 -> fourthSet
            else -> null
        }
    }

    private fun List<GlycemiaPoint>?.ifIsNotNullAppend() {
        if (this == null)
            return
        sets.add(this)
    }

}

@Serializable
data class GlycemiaPoint(
    val date: Long? = null,
    val value: Double,
)
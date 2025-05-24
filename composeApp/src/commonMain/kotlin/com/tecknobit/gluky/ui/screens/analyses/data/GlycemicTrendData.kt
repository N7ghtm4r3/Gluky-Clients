package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.glukycore.AFTERNOON_SNACK_KEY
import com.tecknobit.glukycore.BASAL_INSULIN_KEY
import com.tecknobit.glukycore.FIRST_SET_KEY
import com.tecknobit.glukycore.FOURTH_SET_KEY
import com.tecknobit.glukycore.GLYCEMIC_LABEL_TYPE_KEY
import com.tecknobit.glukycore.MAX_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MEDIUM_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MIN_GLYCEMIC_VALUE_KEY
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

    fun dataAvailable(): Boolean {
        return breakfast != null &&
                morningSnack != null &&
                lunch != null &&
                afternoonSnack != null &&
                dinner != null
    }

    fun firstAvailableDate(): Long? {
        return breakfast?.firstSet?.points?.first()?.date
    }

    fun lastAvailableDate(): Long? {
        return breakfast?.firstSet?.points?.last()?.date
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

}

@Serializable
data class GlycemicTrendData(
    @SerialName(FIRST_SET_KEY)
    val firstSet: GlycemiaTrendDataSet? = null,
    @SerialName(SECOND_SET_KEY)
    val secondSet: GlycemiaTrendDataSet? = null,
    @SerialName(THIRD_SET_KEY)
    val thirdSet: GlycemiaTrendDataSet? = null,
    @SerialName(FOURTH_SET_KEY)
    val fourthSet: GlycemiaTrendDataSet? = null,
    @SerialName(GLYCEMIC_LABEL_TYPE_KEY)
    val labelType: GlycemicTrendLabelType? = null,
) {

    val sets: MutableList<GlycemiaTrendDataSet> = mutableListOf()

    init {
        firstSet.ifIsNotNullAppend()
        secondSet.ifIsNotNullAppend()
        thirdSet.ifIsNotNullAppend()
        fourthSet.ifIsNotNullAppend()
    }

    fun getSpecifiedSet(
        index: Int,
    ): GlycemiaTrendDataSet? {
        return when (index) {
            0 -> firstSet
            1 -> secondSet
            2 -> thirdSet
            3 -> fourthSet
            else -> null
        }
    }

    private fun GlycemiaTrendDataSet?.ifIsNotNullAppend() {
        if (this == null)
            return
        sets.add(this)
    }

}

@Serializable
data class GlycemiaTrendDataSet(
    @SerialName(MAX_GLYCEMIC_VALUE_KEY)
    val maxGlycemicValue: Int,
    @SerialName(MIN_GLYCEMIC_VALUE_KEY)
    val minGlycemicValue: Int,
    @SerialName(MEDIUM_GLYCEMIC_VALUE_KEY)
    val mediumGlycemicValue: Double,
    val points: List<GlycemiaPoint>,
)

@Serializable
data class GlycemiaPoint(
    val date: Long,
    val value: Int,
)

package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.glukycore.FIRST_SET_KEY
import com.tecknobit.glukycore.FOURTH_SET_KEY
import com.tecknobit.glukycore.GLYCEMIC_LABEL_TYPE_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.MAX_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MAX_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.MEDIUM_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MEDIUM_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.MIN_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MIN_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.SECOND_SET_KEY
import com.tecknobit.glukycore.THIRD_SET_KEY
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    @SerialName(MAX_INSULIN_VALUE_KEY)
    val maxInsulinValue: Int,
    @SerialName(MIN_INSULIN_VALUE_KEY)
    val minInsulinValue: Int,
    @SerialName(MEDIUM_INSULIN_VALUE_KEY)
    val mediumInsulinValue: Double,
    val set: List<GlycemiaPoint>,
)

@Serializable
data class GlycemiaPoint(
    val date: Long,
    val value: Int,
    @SerialName(INSULIN_UNITS_KEY)
    val insulinUnits: Int,
)

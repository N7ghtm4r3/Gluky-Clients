package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendDataSet.Companion.ifIsNotNull
import com.tecknobit.glukycore.FIFTH_SET_KEY
import com.tecknobit.glukycore.FIRST_SET_KEY
import com.tecknobit.glukycore.FOURTH_SET_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.MAX_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MAX_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.MEDIUM_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MEDIUM_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.MIN_GLYCEMIC_VALUE_KEY
import com.tecknobit.glukycore.MIN_INSULIN_VALUE_KEY
import com.tecknobit.glukycore.SECOND_SET_KEY
import com.tecknobit.glukycore.SIXTH_SET_KEY
import com.tecknobit.glukycore.THIRD_SET_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlycemiaTrendData(
    @SerialName(FIRST_SET_KEY)
    val firstSet: GlycemiaTrendDataSet? = null,
    @SerialName(SECOND_SET_KEY)
    val secondSet: GlycemiaTrendDataSet? = null,
    @SerialName(THIRD_SET_KEY)
    val thirdSet: GlycemiaTrendDataSet? = null,
    @SerialName(FOURTH_SET_KEY)
    val fourthSet: GlycemiaTrendDataSet? = null,
    @SerialName(FIFTH_SET_KEY)
    val fifthSet: GlycemiaTrendDataSet? = null,
    @SerialName(SIXTH_SET_KEY)
    val sixthSet: GlycemiaTrendDataSet? = null,
) {

    val sets: MutableList<GlycemiaTrendDataSet> = mutableListOf()

    init {
        firstSet.ifIsNotNull {
            sets.add(it)
        }
        secondSet.ifIsNotNull {
            sets.add(it)
        }
        thirdSet.ifIsNotNull {
            sets.add(it)
        }
        fourthSet.ifIsNotNull {
            sets.add(it)
        }
        fifthSet.ifIsNotNull {
            sets.add(it)
        }
        sixthSet.ifIsNotNull {
            sets.add(it)
        }
    }

}

@Serializable
data class GlycemiaTrendDataSet(
    @SerialName(MAX_GLYCEMIC_VALUE_KEY)
    val maxGlycemicValue: Double,
    @SerialName(MIN_GLYCEMIC_VALUE_KEY)
    val minGlycemicValue: Double,
    @SerialName(MEDIUM_GLYCEMIC_VALUE_KEY)
    val mediumGlycemicValue: Double,
    @SerialName(MAX_INSULIN_VALUE_KEY)
    val maxInsulinValue: Double,
    @SerialName(MIN_INSULIN_VALUE_KEY)
    val minInsulinValue: Double,
    @SerialName(MEDIUM_INSULIN_VALUE_KEY)
    val mediumInsulinValue: Double,
    val set: List<GlycemiaPoint>,
) {

    companion object {

        fun GlycemiaTrendDataSet?.ifIsNotNull(
            then: (GlycemiaTrendDataSet) -> Unit,
        ) {
            if (this == null)
                return
            then(this)
        }

    }

}

@Serializable
data class GlycemiaPoint(
    val date: Long,
    val value: Double,
    @SerialName(INSULIN_UNITS_KEY)
    val insulinUnits: Int,
)

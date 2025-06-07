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

/**
 * The `GlycemicTrendDataContainer` data class is used as container of the data related to the
 * glycemic trend over a selected period
 *
 * @property breakfast The trend data related to the breakfast measurements
 * @property morningSnack The trend data related to the morning snack measurements
 * @property lunch The trend data related to the lunch measurements
 * @property afternoonSnack The trend data related to the afternoon snack measurements
 * @property dinner The trend data related to the dinner measurements
 * @property basalInsulin The trend data related to the basal insulin measurements
 * @property from The start date from retrieve the measurements
 * @property to The end date from retrieve the measurements
 *
 * @author N7ghtm4r3 - Tecknobit
 */
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
    val from: Long = -1,
    val to: Long = -1,
) {

    /**
     * `availableSets` the list of available sets
     */
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

    /**
     * Method used to check whether the data are available
     *
     * @return whether the data are available as [Boolean]
     */
    fun dataAvailable(): Boolean {
        return ((breakfast != null && breakfast.hasDataAvailable()) ||
                (morningSnack != null && morningSnack.hasDataAvailable()) ||
                (lunch != null && lunch.hasDataAvailable()) ||
                (afternoonSnack != null && afternoonSnack.hasDataAvailable()) ||
                (dinner != null && dinner.hasDataAvailable()))
    }

    /**
     * Utility method used to append to [availableSets] a set of data if is not null
     *
     * @param type The type of the [GlycemicTrendData] to append
     */
    private fun GlycemicTrendData?.ifIsNotNullAppend(
        type: MeasurementType,
    ) {
        if (this == null)
            return
        availableSets.add(type)
    }

    /**
     * Method used to retrieve the specific set based on the [type]
     *
     * @param type The type of the set to retrieve
     *
     * @return the specific set as nullable [GlycemicTrendData]
     */
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

/**
 * The `GlycemicTrendData` data class is used to contains the trend data for each type of measurement
 *
 * @property higherGlycemia The higher glycemia value
 * @property lowerGlycemia The lower glycemia value
 * @property averageGlycemia The average glycemia value
 * @property firstSet The first set of data
 * @property secondSet The second set of data
 * @property thirdSet The third set of data
 * @property fourthSet The fourth set of data
 * @property labelType The type of the label
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class GlycemicTrendData(
    @SerialName(HIGHER_GLYCEMIA_KEY)
    val higherGlycemia: GlycemiaPoint,
    @SerialName(LOWER_GLYCEMIA_KEY)
    val lowerGlycemia: GlycemiaPoint,
    @SerialName(AVERAGE_GLYCEMIA_KEY)
    val averageGlycemia: Double,
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

    /**
     * `sets` the list of available sets
     */
    val sets: MutableList<List<GlycemiaPoint>> = mutableListOf()

    init {
        firstSet.ifIsNotNullAppend()
        secondSet.ifIsNotNullAppend()
        thirdSet.ifIsNotNullAppend()
        fourthSet.ifIsNotNullAppend()
    }

    /**
     * Method used to retrieve the specific set based on the [index]
     *
     * @param index The index of the set to retrieve
     *
     * @return the specific set as nullable [List] of [GlycemiaPoint]
     */
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

    /**
     * Method used to check whether the data are available
     *
     * @return whether the data are available as [Boolean]
     */
    fun hasDataAvailable(): Boolean {
        var biggestSet = 0
        sets.forEach { set ->
            val setSize = set.size
            if (setSize > biggestSet)
                biggestSet = setSize
        }
        return biggestSet > 1
    }

    /**
     * Utility method used to append to [sets] a set of points if is not null
     */
    private fun List<GlycemiaPoint>?.ifIsNotNullAppend() {
        if (this == null)
            return
        sets.add(this)
    }

}

/**
 * The `GlycemiaPoint` data class is useful to represent a value on a chart
 *
 * @property date The date when the value has been annotated
 * @property value The glycemic value
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class GlycemiaPoint(
    val date: Long,
    val value: Double,
)
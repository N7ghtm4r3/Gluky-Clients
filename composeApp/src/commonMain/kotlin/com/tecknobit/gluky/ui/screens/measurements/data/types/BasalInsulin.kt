package com.tecknobit.gluky.ui.screens.measurements.data.types

import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `BasalInsulin` data class represents the measurement of the basal insulin
 *
 * @property id The identifier of the basal insulin measurement
 * @property _annotationDate The date when the measurement has been annotated
 * @property _glycemia The value of the glycemia when annotated
 * @property _insulinUnits The value of the administered insulin units related to the [_glycemia] value
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see GlycemicMeasurementItem
 */
@Serializable
data class BasalInsulin(
    override val id: String,
    @SerialName(ANNOTATION_DATE_KEY)
    override val _annotationDate: Long = -1,
    @SerialName(GLYCEMIA_KEY)
    override val _glycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    override val _insulinUnits: Int = -1,
) : GlycemicMeasurementItem() {

    /**
     * `isGlycemiaFilled` whether the [glycemia] is filled
     */
    val isGlycemiaFilled: Boolean
        get() = glycemia.value != -1

    /**
     * `isNotFilledYet` whether the item has been filled
     */
    override val isNotFilledYet: Boolean
        get() = annotationDate.value == -1L &&
                glycemia.value == -1 &&
                insulinUnits.value == -1

}
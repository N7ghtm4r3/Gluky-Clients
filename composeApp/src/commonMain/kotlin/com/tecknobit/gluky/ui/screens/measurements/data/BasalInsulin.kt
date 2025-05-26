package com.tecknobit.gluky.ui.screens.measurements.data

import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    val isGlycemiaFilled: Boolean
        get() = glycemia.value != -1

    override val isNotFilledYet: Boolean
        get() = annotationDate.value == -1L &&
                glycemia.value == -1 &&
                insulinUnits.value == -1

}
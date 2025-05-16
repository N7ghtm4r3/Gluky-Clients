package com.tecknobit.gluky.ui.screens.meals.data

import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.enums.MeasurementType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val id: String,
    val type: MeasurementType,
    @SerialName(ANNOTATION_DATE_KEY)
    val annotationDate: Long,
    val content: String,
    val glycemia: Int,
    @SerialName(INSULIN_UNITS_KEY)
    val insulinUnits: Int,
)

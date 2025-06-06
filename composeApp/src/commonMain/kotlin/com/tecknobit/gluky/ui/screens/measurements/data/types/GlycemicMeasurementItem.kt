package com.tecknobit.gluky.ui.screens.measurements.data.types

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

/**
 * The `GlycemicMeasurementItem` data class represents an item which handle glycemic measurements such
 * glycemia and related insulin units
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
sealed class GlycemicMeasurementItem {

    /**
     * `id` the identifier of the item
     */
    protected abstract val id: String

    /**
     * `_annotationDate` the date when the item has been annotated
     */
    @Suppress("PropertyName")
    protected abstract val _annotationDate: Long

    /**
     * `_glycemia` the value of the glycemia when annotated
     */
    @Suppress("PropertyName")
    protected abstract val _glycemia: Int

    /**
     * `_insulinUnits` the value of the administered insulin units related to the [glycemia] value
     */
    @Suppress("PropertyName")
    protected abstract val _insulinUnits: Int

    /**
     * `annotationDate` state for the date when the item has been annotated
     */
    val annotationDate: MutableState<Long> by lazy { mutableStateOf(_annotationDate) }

    /**
     * `glycemia` state for the value of the glycemia when annotated
     */
    val glycemia: MutableState<Int> by lazy { mutableStateOf(_glycemia) }

    /**
     * `insulinUnits` state for the value of the administered insulin units related to the [glycemia] value
     */
    val insulinUnits: MutableState<Int> by lazy { mutableStateOf(_insulinUnits) }

    /**
     * `isNotFilledYet` whether the item has been filled
     */
    abstract val isNotFilledYet: Boolean

}
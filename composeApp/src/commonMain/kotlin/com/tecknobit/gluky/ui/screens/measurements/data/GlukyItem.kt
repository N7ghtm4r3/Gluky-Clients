package com.tecknobit.gluky.ui.screens.measurements.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
sealed class GlukyItem {

    protected abstract val id: String

    @Suppress("PropertyName")
    protected abstract val _annotationDate: Long

    @Suppress("PropertyName")
    protected abstract val _glycemia: Int

    @Suppress("PropertyName")
    protected abstract val _insulinUnits: Int

    val annotationDate: MutableState<Long> by lazy { mutableStateOf(_annotationDate) }

    val glycemia: MutableState<Int> by lazy { mutableStateOf(_glycemia) }

    val insulinUnits: MutableState<Int> by lazy { mutableStateOf(_insulinUnits) }

    abstract val isNotFilledYet: Boolean

}
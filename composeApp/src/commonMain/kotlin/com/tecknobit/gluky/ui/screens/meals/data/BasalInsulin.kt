package com.tecknobit.gluky.ui.screens.meals.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.tecknobit.gluky.ui.screens.shared.data.GlukyItem
import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class BasalInsulin(
    override val id: String,
    @SerialName(ANNOTATION_DATE_KEY)
    override val annotationDate: Long = -1,
    @SerialName(GLYCEMIA_KEY)
    private val _glycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    private val _insulinUnits: Int = -1,
) : GlukyItem {

    @Transient
    val glycemia: MutableState<Int> = mutableStateOf(_glycemia)

    @Transient
    val insulinUnits: MutableState<Int> = mutableStateOf(_insulinUnits)

    override val isNotFilledYet: Boolean
        get() = annotationDate != -1L &&
                glycemia.value == -1 &&
                insulinUnits.value == -1

}
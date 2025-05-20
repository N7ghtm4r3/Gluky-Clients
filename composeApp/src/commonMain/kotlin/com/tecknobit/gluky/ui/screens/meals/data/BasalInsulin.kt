package com.tecknobit.gluky.ui.screens.meals.data

import com.tecknobit.gluky.ui.screens.shared.data.GlukyItem
import kotlinx.serialization.Serializable

@Serializable
data class BasalInsulin(
    override val id: String,
    val glycemia: Int = -1,
    val insulinUnits: Int = -1,
) : GlukyItem {

    override val isNotFilledYet: Boolean
        get() = glycemia == -1 && insulinUnits == -1

}
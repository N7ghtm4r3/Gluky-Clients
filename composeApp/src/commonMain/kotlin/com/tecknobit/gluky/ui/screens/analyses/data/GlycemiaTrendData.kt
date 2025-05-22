package com.tecknobit.gluky.ui.screens.analyses.data

import kotlinx.serialization.Serializable

@Serializable
data class GlycemiaTrendData(
    val firstSet: GlycemiaTrendDataSet? = null,
    val secondSet: GlycemiaTrendDataSet? = null,
    val thirdSet: GlycemiaTrendDataSet? = null,
    val fourthSet: GlycemiaTrendDataSet? = null,
    val fifthSet: GlycemiaTrendDataSet? = null,
    val sixthSet: GlycemiaTrendDataSet? = null,
)

@Serializable
data class GlycemiaTrendDataSet(
    val maxGlycemicValue: Double,
    val minGlycemicValue: Double,
    val mediumGlycemicValue: Double,
    val maxInsulinValue: Double,
    val minInsulinValue: Double,
    val mediumInsulinValue: Double,
    val set: List<GlycemiaPoint>,
)

@Serializable
data class GlycemiaPoint(
    val date: Long,
    val value: Double,
    val insulinUnits: Int,
)

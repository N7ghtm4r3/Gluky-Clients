package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.glukycore.REPORT_IDENTIFIER_KEY
import com.tecknobit.glukycore.REPORT_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Report(
    @SerialName(REPORT_IDENTIFIER_KEY)
    val reportId: String,
    val name: String,
    @SerialName(REPORT_URL)
    val reportUrl: String,
)

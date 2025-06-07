package com.tecknobit.gluky.ui.screens.analyses.data

import com.tecknobit.glukycore.REPORT_IDENTIFIER_KEY
import com.tecknobit.glukycore.REPORT_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `Report` data class is used to contains the details of the created report
 *
 * @property reportId  The identifier of the report
 * @property name      The name of the report
 * @property reportUrl The url where the report can be downloaded
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class Report(
    @SerialName(REPORT_IDENTIFIER_KEY)
    val reportId: String,
    val name: String,
    @SerialName(REPORT_URL)
    val reportUrl: String,
)

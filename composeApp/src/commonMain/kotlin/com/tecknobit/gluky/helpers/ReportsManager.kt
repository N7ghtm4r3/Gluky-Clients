package com.tecknobit.gluky.helpers

/**
 * Method used to save a report after its download
 *
 * @param reportBytes The bytes made-up the report
 * @param reportName The name of the report, it used to name the file also
 * @param onSave Callback invoked when the file has been saved
 */
expect suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onSave: (String?) -> Unit,
)

/**
 * Method used to open the report file when saved
 *
 * @param url The url of the report to open it
 */
expect fun openReport(
    url: String?,
)
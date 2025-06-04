package com.tecknobit.gluky.helpers

expect suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (String?) -> Unit,
)

expect fun openReport(
    url: String?,
)
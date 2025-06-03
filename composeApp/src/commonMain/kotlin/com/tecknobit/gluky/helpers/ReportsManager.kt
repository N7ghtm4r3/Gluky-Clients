package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.PlatformFile

expect suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (PlatformFile) -> Unit,
)

expect fun openReport(
    reportFile: PlatformFile,
)
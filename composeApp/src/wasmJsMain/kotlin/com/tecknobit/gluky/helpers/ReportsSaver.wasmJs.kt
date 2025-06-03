package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
) {
    val downloadedReport = FileKit.download(
        bytes = reportBytes,
        fileName = ""
    )
}
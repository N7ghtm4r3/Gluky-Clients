package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.utils.toJsArray
import org.w3c.files.File

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (PlatformFile) -> Unit,
) {
    val downloadedReport = PlatformFile(
        File(
            fileBits = reportBytes.toJsArray(),
            fileName = reportName
        )
    )
    FileKit.download(
        file = downloadedReport,
        fileName = reportName
    )
    onDownloadCompleted(downloadedReport)
}

actual fun openReport(reportFile: PlatformFile) {
}
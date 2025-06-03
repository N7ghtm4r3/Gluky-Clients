package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.write

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (PlatformFile) -> Unit,
) {
    val downloadedReport = PlatformFile(FileKit.filesDir, reportName)
    downloadedReport.write(reportBytes)
    onDownloadCompleted(downloadedReport)
}

actual fun openReport(reportFile: PlatformFile) {
}
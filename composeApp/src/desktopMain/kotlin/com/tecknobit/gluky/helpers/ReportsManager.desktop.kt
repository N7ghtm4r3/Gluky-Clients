package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.downloadDir
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.write
import java.awt.Desktop
import java.io.File

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (String?) -> Unit,
) {
    val downloadedReport = PlatformFile(FileKit.downloadDir, reportName)
    downloadedReport.write(reportBytes)
    onDownloadCompleted(downloadedReport.path)
}

actual fun openReport(
    url: String?,
) {
    url?.let {
        val report = File(url)
        Desktop.getDesktop().open(report)
    }
}
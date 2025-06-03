package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.downloadDir
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.awt.Desktop

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (PlatformFile) -> Unit,
) {
    val downloadedReport = PlatformFile(FileKit.downloadDir, reportName)
    downloadedReport.write(reportBytes)
    onDownloadCompleted(downloadedReport)
}

actual fun openReport(
    reportFile: PlatformFile,
) {
    MainScope().launch {
        Desktop.getDesktop().open(reportFile.file)
    }
}
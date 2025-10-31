package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.github.vinceglb.filekit.downloadDir
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.write

/**
 * Method used to save a report after its download
 *
 * @param reportBytes The bytes made-up the report
 * @param reportName The name of the report, it used to name the file also
 * @param onSave Callback invoked when the file has been saved
 */
actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onSave: (String?) -> Unit,
) {
    val downloadedReport = PlatformFile(FileKit.downloadDir, reportName)
    downloadedReport.write(reportBytes)
    onSave(downloadedReport.path)
}

/**
 * Method used to open the report file when saved
 *
 * @param url The url of the report to open it
 */
actual fun openReport(
    url: String?,
) {
    url?.let {
        FileKit.openFileWithDefaultApplication(
            file = PlatformFile(
                path = url
            )
        )
    }
}
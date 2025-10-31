@file:OptIn(ExperimentalWasmJsInterop::class)

package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.utils.toJsArray
import org.w3c.files.File

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
    onSave(downloadedReport.name)
}

/**
 * Method used to open the report file when saved
 *
 * @param url The url of the report to open it
 */
actual fun openReport(
    url: String?,
) {
}
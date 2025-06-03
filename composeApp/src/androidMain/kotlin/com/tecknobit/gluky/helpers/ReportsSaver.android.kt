package com.tecknobit.gluky.helpers

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.dialogs.deprecated.openFileSaver
import io.github.vinceglb.filekit.filesDir

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
) {
    val downloadedReport = FileKit.openFileSaver(
        bytes = reportBytes,
        suggestedName = reportName
    )
    val destinationLocation = PlatformFile(FileKit.filesDir, "document.txt")
    downloadedReport?.copyTo(destinationLocation)
}
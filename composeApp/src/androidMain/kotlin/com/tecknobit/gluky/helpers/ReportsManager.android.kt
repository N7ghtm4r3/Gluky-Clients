package com.tecknobit.gluky.helpers

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore.Downloads
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import androidx.core.net.toUri
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.context
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
    val downloadedReport = FileKit.saveToDownloads(
        bytes = reportBytes,
        filename = reportName
    ) ?: throw IllegalStateException()
    onSave(downloadedReport.path)
}

/**
 * Method used to save a file into the [DIRECTORY_DOWNLOADS] folder
 *
 * @param bytes The bytes made-up the file
 * @param filename The name used to save the file
 *
 * @return the uri of the file saved as nullable [Uri]
 */
private suspend fun FileKit.saveToDownloads(
    bytes: ByteArray,
    filename: String,
): Uri? = withContext(Dispatchers.IO) {
    return@withContext if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        val location = File(
            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),
            filename
        )
        location.writeBytes(bytes)
        location.toUri()
    } else {
        val contentValues = ContentValues().apply {
            put(Downloads.DISPLAY_NAME, filename)
            put(Downloads.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val collection = Downloads.getContentUri(VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)
        itemUri?.let {
            resolver.openOutputStream(itemUri)?.use { outputStream ->
                outputStream.write(bytes)
            }
            contentValues.clear()
            contentValues.put(Downloads.IS_PENDING, 0)
            resolver.update(itemUri, contentValues, null, null)
        }
        itemUri
    }
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
                uri = url.toUri()
            )
        )
    }
}
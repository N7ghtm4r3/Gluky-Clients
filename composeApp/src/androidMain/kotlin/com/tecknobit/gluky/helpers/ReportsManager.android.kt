package com.tecknobit.gluky.helpers

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore.Downloads
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import androidx.core.net.toUri
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (String?) -> Unit,
) {
    val downloadedReport = FileKit.saveToDownloads(
        bytes = reportBytes,
        filename = reportName
    ) ?: throw IllegalStateException()
    onDownloadCompleted(downloadedReport.path)
}

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

actual fun openReport(
    url: String?,
) {
    url?.let {
        val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        FileKit.context.startActivity(intent)
    }
}
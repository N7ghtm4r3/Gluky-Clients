package com.tecknobit.gluky.helpers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication

actual suspend fun saveReport(
    reportBytes: ByteArray,
    reportName: String,
    onDownloadCompleted: (String?) -> Unit,
) {
    val documents = NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).firstOrNull()
    if (documents == null)
        throw IllegalStateException()
    val path = "$documents/$reportName"
    val data = reportBytes.toNSData()
    data.writeToFile(path, true)
    onDownloadCompleted(path)
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = this.usePinned { pinned ->
    NSData.dataWithBytes(
        bytes = pinned.addressOf(0),
        length = size.toULong()
    )
}

actual fun openReport(
    url: String?,
) {
    url?.let {
        val nsUrl = NSURL.URLWithString(
            URLString = url
        )!!
        UIApplication.sharedApplication.openURL(
            url = nsUrl
        )
    }
}
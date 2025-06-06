package com.tecknobit.gluky.helpers

import com.tecknobit.equinoxcore.annotations.Returner
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
    onSave(path)
}

/**
 * Method used to convert a [ByteArray] into [NSData] object,
 * will be blocked in memory the original byte array and then used the first address
 * of the array to convert that array
 *
 * @return the byte array converted as [NSData]
 */
@OptIn(ExperimentalForeignApi::class)
@Returner
fun ByteArray.toNSData(): NSData = this.usePinned { pinned ->
    NSData.dataWithBytes(
        bytes = pinned.addressOf(0),
        length = size.toULong()
    )
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
        val nsUrl = NSURL.URLWithString(
            URLString = url
        )!!
        UIApplication.sharedApplication.openURL(
            url = nsUrl
        )
    }
}
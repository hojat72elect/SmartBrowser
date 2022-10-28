package com.duckduckgo.downloads.api

import java.io.File
import java.io.Serializable
import android.os.Environment
import androidx.annotation.AnyThread

/** Interface for the starting point of a download. */
interface FileDownloader {

    /** Starts a download. Takes as parameters a [PendingFileDownload] containing all details about the file to be downloaded. */
    @AnyThread
    fun enqueueDownload(pending: PendingFileDownload)

    /** Data class for pending download.*/
    data class PendingFileDownload(
        val url: String,
        val contentDisposition: String? = null,
        val mimeType: String? = null,
        val subfolder: String,
        val directory: File = Environment.getExternalStoragePublicDirectory(subfolder),
        val isUrlCompressed: Boolean = false
    ) : Serializable
}

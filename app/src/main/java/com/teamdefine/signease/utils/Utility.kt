package com.teamdefine.signease.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*


object Utility {
    fun Context.toast(message: String) {
        Toast.makeText(
            this, message,
            if (message.length <= 25) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    }

    fun Fragment.toast(msg: String) {
        requireContext().toast(msg)
    }

    fun Context.downloadFile(fileUrl: String, fileTitle: String, fileName: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(fileUrl))
        request.setTitle(fileTitle)
        request.setDescription("Downloading")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(request)

    }
    fun Fragment.downloadFile(fileUrl: String, fileTitle: String, fileName: String) {
        requireContext().downloadFile(fileUrl, fileTitle, fileName)
    }

    fun getCurrentDate() = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
}
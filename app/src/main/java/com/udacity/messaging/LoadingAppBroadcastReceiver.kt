package com.udacity.messaging

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import com.udacity.utils.statusToStr
import timber.log.Timber

class LoadingAppBroadcastReceiver() : BroadcastReceiver() {

    @SuppressLint("Range")
    override fun onReceive(contxt: Context?, intent: Intent?) {
        Timber.i("onReceive: $intent (ctx: $contxt)")


        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        val query = DownloadManager.Query()
        if (id != null) {
            query.setFilterById(id)
        }
        val downloadManager = contxt!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val c = downloadManager.query(query)

        if (c.moveToFirst() && c.getColumnIndex(DownloadManager.COLUMN_STATUS) >= 0) {
            val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            c.close()
            Timber.i("download status for id $id: ${statusToStr(status)}")
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    Timber.i("YES!!!")

                }

                DownloadManager.STATUS_FAILED -> {
                    Timber.w("Download failed")
                }

            }
        }
        when (intent?.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> handleDownloadComplete()
        }


    }

    private fun handleDownloadComplete() {
        Timber.i("Download complete!")
    }
}
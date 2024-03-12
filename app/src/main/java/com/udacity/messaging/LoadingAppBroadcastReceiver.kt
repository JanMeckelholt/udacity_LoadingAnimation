package com.udacity.messaging

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.udacity.Constants
import com.udacity.models.DownloadType
import com.udacity.utils.Status
import com.udacity.utils.sendNotification
import com.udacity.utils.statusToStr
import timber.log.Timber


class LoadingAppBroadcastReceiver : BroadcastReceiver() {

    @SuppressLint("Range")
    override fun onReceive(ctx: Context?, intent: Intent?) {

        Repository.instance().setDownloadIsCompleted(true)

        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val query = DownloadManager.Query()
        if (id != null) {
            query.setFilterById(id)
        }
        val downloadManager = ctx!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val c = downloadManager.query(query)

        if (c.moveToFirst() && c.getColumnIndex(DownloadManager.COLUMN_STATUS) >= 0) {
            val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            c.close()
            Timber.i("download status for id $id: ${statusToStr(status)}")
            val notificationManager = ContextCompat.getSystemService(ctx, NotificationManager::class.java) as NotificationManager
            val sharedPreferences = ctx.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
            val json: String? = sharedPreferences.getString(Constants.SHAREDPREF_DOWNLOAD_TYPE, null)
            var downloadType: DownloadType
            json.let {
                downloadType = Gson().fromJson(it, DownloadType::class.java)
            }
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    Timber.i("Download success: $id")
                    downloadType.status = Status.SUCCESS
                    sharedPreferences.edit().putString(Constants.SHAREDPREF_DOWNLOAD_TYPE, Gson().toJson(downloadType)).apply()
                    notificationManager.sendNotification(ctx.applicationContext, downloadType)
                }

                DownloadManager.STATUS_FAILED -> {
                    Timber.w("Download failed")
                    downloadType.status = Status.FAILED
                    sharedPreferences.edit().putString(Constants.SHAREDPREF_DOWNLOAD_TYPE, Gson().toJson(downloadType)).apply()
                    notificationManager.sendNotification(ctx.applicationContext, downloadType)
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


class Repository private constructor() {
    private val _repIsDownloadCompleted = MutableLiveData<Boolean?>()
    val repIsDownloadCompleted: LiveData<Boolean?>
        get() = _repIsDownloadCompleted


    fun setDownloadIsCompleted(isCompleted: Boolean){
        _repIsDownloadCompleted.value = isCompleted
    }


    companion object {
        private val INSTANCE = Repository()
        fun instance(): Repository {
            return INSTANCE
        }
    }
}

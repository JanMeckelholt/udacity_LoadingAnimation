package com.udacity.activities

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.udacity.Constants
import com.udacity.R
import com.udacity.databinding.ActivityMainBinding
import com.udacity.models.DownloadType
import com.udacity.utils.getUrl
import timber.log.Timber


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }
    private lateinit var binding: ActivityMainBinding
    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.content.rgDownloadSelection.setOnCheckedChangeListener { _, selection ->
            binding.content.customButton.setIsReadyToDownload(true)
            binding.content.customButton.setUrlIsSelected((selection != -1))
        }

        binding.content.customButton.setOnClickListener {
            Timber.i("selection: ${binding.content.rgDownloadSelection.checkedRadioButtonId}")
            if (binding.content.rgDownloadSelection.checkedRadioButtonId == -1) {
                Toast.makeText(this, getString(R.string.no_selection), Toast.LENGTH_SHORT).show()
            } else {
                download(binding.content.rgDownloadSelection.checkedRadioButtonId)
            }
        }

        viewModel.isDownloadCompleted.observe(this, Observer { isDownloadCompleted ->
            Timber.w("observing change: - $isDownloadCompleted -")
            isDownloadCompleted?.let {
                binding.content.customButton.setDownloadFinished(it)
                //viewModel.setDownloadIsCompleted(false);
            }
        })

        createChannel()

    }


    private fun download(selection: Int) {
        Timber.i("downloading $selection -> ${Uri.parse(getUrl(selection))}")
        getUrl(selection)?.let {
            val request =
                DownloadManager.Request(Uri.parse(it))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)

            val json = Gson().toJson(DownloadType(downloadID, selection))
            sharedPreferences.edit().putString(Constants.SHAREDPREF_DOWNLOAD_TYPE, json).apply()
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = Constants.CHANNEL_DESC
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}


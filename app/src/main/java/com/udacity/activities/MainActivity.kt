package com.udacity.activities

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.udacity.Constants
import com.udacity.R
import com.udacity.databinding.ActivityMainBinding
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

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.content.rgDownloadSelection.setOnCheckedChangeListener { _, selection ->
            binding.content.customButton.setUrlIsSelected((selection != -1))
        }

        binding.content.customButton.setOnClickListener {
            Timber.i("selection: ${binding.content.rgDownloadSelection.checkedRadioButtonId}")
            if (binding.content.rgDownloadSelection.checkedRadioButtonId == -1) {
                Toast.makeText(this, getString(R.string.no_selection), Toast.LENGTH_SHORT).show()
            } else {
                //download()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(Constants.URL_UDACITY))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }
}
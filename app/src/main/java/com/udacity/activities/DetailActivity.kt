package com.udacity.activities

import android.app.NotificationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.udacity.Constants
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.models.DownloadType

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var downloadType: DownloadType
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(Constants.NOTIFICATION_DEFAULT_ID)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val json: String? = sharedPreferences.getString(Constants.SHAREDPREF_DOWNLOAD_TYPE, null)
        json.let {
            downloadType = Gson().fromJson(it, DownloadType::class.java)
            binding.downloadType = downloadType
        }

        binding.fabBack.setOnClickListener{
            finish()
        }
    }


}

package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager


        createChannel(CHANNEL_NAME)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // TODO: Implement code below
        binding.contentMain.customButton.setOnClickListener {
            if (URL == null) {
                Toast.makeText(this, R.string.select_file_toast_message, Toast.LENGTH_SHORT).show()
            } else {
                binding.contentMain.customButton.changeState(ButtonState.Loading)
                download()
//                binding.contentMain.customButton.changeState(ButtonState.Completed)
            }

        }

        binding.contentMain.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.glide_radio_button -> {
                    URL = "https://github.com/bumptech/glide"
                    radioOptionText = getString(R.string.glide_radio_button)
                }

                R.id.loadapp_radio_button -> {
                    URL =
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                    radioOptionText = getString(R.string.loadapp_radio_button)
                }

                R.id.retrofit_radio_button -> {
                    URL = "https://github.com/square/retrofit"
                    radioOptionText = getString(R.string.retrofit_radio_button)
                }
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                var result = downloadManager.query(
                    DownloadManager.Query().setFilterById(id)
                )
                if (result.moveToFirst()) {
                    val status = result.getInt(
                        result.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                    )
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(applicationContext, radioOptionText, Toast.LENGTH_SHORT)
                            .show()
                        notificationManager.sendNotification(
                            applicationContext,
                            radioOptionText,
                            "Success",
                            CHANNEL_ID
                        )
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(applicationContext, radioOptionText, Toast.LENGTH_SHORT)
                            .show()
                        notificationManager.sendNotification(
                            applicationContext,
                            radioOptionText,
                            "Failed",
                            CHANNEL_ID
                        )
                    }
                    binding.contentMain.customButton.changeState(ButtonState.Completed)
                }
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
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
        private var URL: String? = null
        private var radioOptionText: String = ""
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "downlads"
    }

    private fun createChannel(channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            notificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager

            if (!notificationManager.areNotificationsEnabled()) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
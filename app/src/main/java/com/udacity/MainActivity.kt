package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.view.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private var url: String? = null
    private var fileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID, applicationContext.getString(R.string.notification_channel_name))

        custom_button.setOnClickListener {
            when {
                glide_radio.isChecked -> {
                    url = URL_GLIDE
                    fileName = getString(R.string.glide_string)
                    custom_button.buttonState = ButtonState.Loading

                    download()
                }
                loadapp_radio.isChecked -> {
                    url = URL_LOADAPP
                    fileName = getString(R.string.loadapp_string)
                    custom_button.buttonState = ButtonState.Loading

                    download()
                }
                retrofit_radio.isChecked -> {
                    url = URL_RETROFIT
                    fileName = getString(R.string.retrofit_string)
                    custom_button.buttonState = ButtonState.Loading

                    download()
                }
                else -> {
                    Toast.makeText(this, "Select an option", Toast.LENGTH_LONG).show()
                        .toString()

                }
            }
        }
    }

    companion object {
        private const val URL_LOADAPP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE = "https://github.com/bumptech/glide"
        private const val URL_RETROFIT = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                var downloadStatus = "Failed"
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(index)) downloadStatus = "Success"

                Toast.makeText(
                    applicationContext,
                    getString(R.string.notification_description),
                    Toast.LENGTH_LONG).show()

                custom_button.buttonState = ButtonState.Completed

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.sendNotification(
                    CHANNEL_ID,
                    getString(R.string.notification_description),
                    applicationContext,
                    fileName!!,
                    downloadStatus

                )
            }
        }
    }
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.description = "Load File"
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)

            notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
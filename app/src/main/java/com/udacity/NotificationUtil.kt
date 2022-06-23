package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val ID_NOTIFICATION = 0

fun NotificationManager.sendNotification(
    channelId: String,
    messageBody: String,
    applicationContext: Context,
    fileName: String,
    status: String) {

    val detailActivityIntent = Intent(applicationContext, DetailActivity::class.java)
    detailActivityIntent.putExtra("status", status)
    detailActivityIntent.putExtra("fileName", fileName)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        ID_NOTIFICATION,
        detailActivityIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(ID_NOTIFICATION, builder.build())
}
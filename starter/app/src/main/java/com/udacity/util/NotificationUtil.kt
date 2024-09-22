package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private val NOTIFICATION_ID = 1

fun NotificationManager.sendNotification(
    applicationContext: Context,
    fileName: String,
    downloadStatus: String,
    channelId: String
)  {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra("fileName", fileName)
        .putExtra("downloadStatus", downloadStatus)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId,
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.notification_description))
        .addAction(
            0,
            applicationContext.getString(R.string.notification_button),
            pendingIntent)
        .setAutoCancel(true)


    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
package com.example.flutter_foreground_example

import androidx.annotation.RequiresApi
import android.content.Context
import android.graphics.Color
import android.app.Notification
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.Timer
import java.util.TimerTask

class ExampleService : Service() {
    val notificationId = 1
    var serviceRunning = false
    lateinit var builder: NotificationCompat.Builder
    lateinit var channel: NotificationChannel
    lateinit var manager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        startForeground()
        serviceRunning = true
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (serviceRunning == true) {
                    updateNotification("I got updated!")
                }
            }
        }, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceRunning = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        channel = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        return channelId
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("example_service", "Example Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        builder = NotificationCompat.Builder(this, channelId)
        builder
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Example Service")
            .setContentText("Example Serivce is running")
            .setCategory(Notification.CATEGORY_SERVICE)
        startForeground(1, builder.build())
    }

    private fun updateNotification(text: String) {
        builder
            .setContentText(text)
        manager.notify(notificationId, builder.build());
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

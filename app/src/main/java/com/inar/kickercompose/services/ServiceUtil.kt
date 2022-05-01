package com.inar.kickercompose.services

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

object ServiceUtil {
    const val CHANNEL_ID = "com.inar.kickercompose.notif"
    const val RESTART_ACTION = "restartservice"
    const val LOG_TAG = "SignalService"

//    fun createNotificationChannel(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, "kickerChannel", importance).apply {
//                description = "just channel for kicker"
//            }
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}

fun Activity.isMyServiceRunning(serviceClass: Class<*>) {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    manager.getRunningServices(Int.MAX_VALUE)
}
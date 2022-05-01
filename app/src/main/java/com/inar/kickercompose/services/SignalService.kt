package com.inar.kickercompose.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.inar.kickercompose.R
import com.inar.kickercompose.data.net.signal.HubHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SignalService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    companion object {
        const val LOG_TAG = "SignalRService"
    }

    @Inject
    lateinit var hub: HubHandler


    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "SignalR service onCreate")
        createNotifyChannel()

    }

    private fun createNotifyChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ServiceUtil.CHANNEL_ID,
                "kickerChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "just channel for kicker"
            }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, ServiceUtil.CHANNEL_ID)

            val notification: Notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Kicker is running")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            startForeground(2, notification)
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "SignalR service onDestroy")

        Intent().also {
            it.action = ServiceUtil.RESTART_ACTION
            it.setClass(this, Restarter::class.java)
            this.sendBroadcast(it)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startSignalR()
        return START_STICKY
    }


    private fun startSignalR() {
        try {
            hub.start() { i, j ->
                showNotification("just recieved", 101)
            }
        } catch (e: Throwable) {
            showNotification(e.message ?: "throw", 104);
        }

    }

    private fun showNotification(message: String, id: Int) {
        val builder = NotificationCompat.Builder(this, ServiceUtil.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_sports_soccer_24)
            .setContentTitle(message)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(id, builder.build());
        }

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(p0: Intent?): IBinder? = null

}
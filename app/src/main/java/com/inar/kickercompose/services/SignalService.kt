package com.inar.kickercompose.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.inar.kickercompose.R
import com.inar.kickercompose.data.net.signal.HubService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignalService : Service() {

    companion object {
        const val LOG_TAG = "SignalRService"
    }

    @Inject
    lateinit var hub: HubService

    override fun onBind(p0: Intent?): IBinder? {
        //TODO("Not yet implemented")
        return null
    }
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "SignalR service onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "SignalR service onDestroy")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startSignalR()
        return START_REDELIVER_INTENT
    }



    fun startSignalR() {
        hub.start() { i, j ->
            //Toast.makeText(this, "started!", Toast.LENGTH_LONG).show()
            showNotification()
        }
    }

    fun showNotification(){
        val builder = NotificationCompat.Builder(this,ServiceUtil.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_sports_soccer_24)
            .setContentTitle("You were invited")
            .setContentText("Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(101,builder.build());
        }

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

    }
}
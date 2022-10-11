package com.inar.kickercompose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.inar.kickercompose.services.ServiceUtil.CHANNEL_ID

class Restarter : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(CHANNEL_ID, "service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(Intent(context, SignalService::class.java))
//        } else {
//            context.startService(Intent(context, SignalService::class.java))
//        }

        context.applicationContext.startService(Intent(context, SignalService::class.java))
    }
}
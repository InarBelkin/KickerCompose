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


    const val OPEN_LOBBY_EXTRA = "OpenLobby"
    const val inviteMessId = 101
    const val INVITE_MESSAGE_EXTRA = "InviteMessageParcel"
    const val SENDING_ANSWER = "SENDINGANSWER"
    const val IS_ACCEPT = "IsAccept"

    object LobbyObserver {
        const val BROADCAST_ACTION = "com.inar.KickerCompose::lobbyObserver"
        const val LOBBY_MODEL_EXTRA = "LobbyModelExtra"
    }

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


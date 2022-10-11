package com.inar.kickercompose.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.inar.kickercompose.MainActivity
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.net.signal.HubHandler
import com.microsoft.signalr.HubConnectionState
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
        //startSignalR()
        if (!reconnectWorks) scope.launch { rekonnecter() }
        return START_STICKY
    }


    private fun startSignalR() {
        try {
            hub.start()
            hub.inviteEvent = {
                showNotificationInvite(it)
            }
            hub.yourLobbyChanged = { lobby ->
                val intent = Intent(ServiceUtil.LobbyObserver.BROADCAST_ACTION).also {
                    it.putExtra(ServiceUtil.LobbyObserver.LOBBY_MODEL_EXTRA, lobby)
                }
                sendBroadcast(intent)
            }
            hub.yourLobbyDeleted = { withResults, battleId ->
                val intent = Intent(ServiceUtil.LobbyDeleted.BROADCAST_ACTION).also {
                    it.putExtra(ServiceUtil.LobbyDeleted.WITH_RESULTS, withResults)
                    it.putExtra(ServiceUtil.LobbyDeleted.BATTLE_ID, battleId)
                }
                sendBroadcast(intent)
            }
        } catch (e: Throwable) {
            showNotification(e.message ?: "throw", 104)
        }

    }

    private fun showNotificationInvite(message: InviteMessage) {
        scope.launch {
            val claims = hub.account.getUserClaims()
            if (message.senderId == (claims?.id ?: "")) return@launch

            message.invitedId = claims?.id ?: ""
            val intent = Intent(this@SignalService, MainActivity::class.java).also {
                it.putExtra(ServiceUtil.OPEN_LOBBY_EXTRA, true)
                it.putExtra(ServiceUtil.InviteAnswer.INVITE_MESSAGE_EXTRA, message)
            }

            val peningIntent =
                PendingIntent.getActivity(this@SignalService,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val noIntent = Intent(application, InviteAnswersBroadcastReceiver::class.java).also {
                it.putExtra(ServiceUtil.InviteAnswer.SENDING_ANSWER, true)
                it.putExtra(ServiceUtil.InviteAnswer.INVITE_MESSAGE_EXTRA, message)
                it.putExtra(ServiceUtil.InviteAnswer.IS_ACCEPT, false)
            }

            val noPendingIntent = PendingIntent.getBroadcast(this@SignalService,
                0,
                noIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(this@SignalService, ServiceUtil.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_sports_soccer_24)
                .setContentTitle("${message.senderName} invite you to a battle!")
                .setContentText(message.message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_baseline_check_24, "Accept", peningIntent)
                .addAction(R.drawable.ic_baseline_clear_24, "Refuse", noPendingIntent)

            with(NotificationManagerCompat.from(this@SignalService)) {
                notify(ServiceUtil.inviteMessId, builder.build())
            }
        }

    }

    private fun sendInviteAnswer(message: InviteMessage, isAccept: Boolean) {
        scope.launch {
            val answer = InviteAnswer().apply {
                invitedId = hub.account.getUserClaims()?.id ?: ""
                initiatorId = message.senderId
                accepted = isAccept
                side = message.side
                position = message.position
            }

            hub.sendInviteAnswer(answer)
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

    private var reconnectWorks: Boolean = false
    private suspend fun rekonnecter() {
        reconnectWorks = true

        while (true) {
            if (!hub.isHubInitialized() || hub.hub.connectionState == HubConnectionState.DISCONNECTED) startSignalR();
            delay(5000)
        }
    }
}
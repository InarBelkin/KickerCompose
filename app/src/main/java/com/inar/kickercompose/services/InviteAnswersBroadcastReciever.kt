package com.inar.kickercompose.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.net.signal.HubHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InviteAnswersBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var hub: HubHandler
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getBooleanExtra(ServiceUtil.InviteAnswer.SENDING_ANSWER, false)) {
            val message =
                intent.getParcelableExtra<InviteMessage>(ServiceUtil.InviteAnswer.INVITE_MESSAGE_EXTRA)
            val isAccept = intent.getBooleanExtra(ServiceUtil.InviteAnswer.IS_ACCEPT, false)
            sendInviteAnswer(message!!, isAccept)

            with(NotificationManagerCompat.from(context)) {
                cancel(ServiceUtil.inviteMessId)
            }
        }

        Log.i("broadcast", "cancel invite")
        //Toast.makeText(context, "Reciever is working", Toast.LENGTH_SHORT).show();

    }

    private fun sendInviteAnswer(message: InviteMessage, isAccept: Boolean) {
        val answer = InviteAnswer().apply {
            invitedId = message.invitedId
            initiatorId = message.senderId
            accepted = isAccept
            side = message.side
            position = message.position
        }

        hub.sendInviteAnswer(answer)
    }
}
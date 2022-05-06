package com.inar.kickercompose.data.net.signal

import android.content.Context
import android.os.UserManager
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.net.repositories.ILobbyRepository
import com.inar.kickercompose.data.net.repositories.LobbyRepository
import com.inar.kickercompose.data.viemodels.AccountHandler
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.rxObservable
import kotlinx.coroutines.rx3.rxSingle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubHandler @Inject constructor(
    val account: AccountHandler,
    @ApplicationContext private val appContext: Context,
    private val lobby: ILobbyRepository,
) {
    lateinit var hub: HubConnection

    fun start() { //TODO: try to change callback to events(or how does it called here?)
        val url = appContext.getString(R.string.baseUrl) + "invitation"

        hub = HubConnectionBuilder.create(url)
            .withAccessTokenProvider(
                rxSingle {
                    account.getAccessToken() ?: ""
                }
            ).build()

        hub.on("Invite", { m ->
            inviteEvent?.invoke(m)
        }, InviteMessage::class.java)


        hub.on("AnswerInvite", { m ->
            answerInviteEvent?.invoke(m)
        }, InviteAnswer::class.java)


        hub.on("YourLobbyChanged", { m ->
            yourLobbyChanged?.invoke(m)
        }, LobbyItemModel::class.java)

        hub.on("YourLobbyDeleted") {
            yourLobbyDeleted?.invoke()
        }

        hub.start()
    }

    var inviteEvent: ((InviteMessage) -> Unit)? = null
    var answerInviteEvent: ((InviteAnswer) -> Unit)? = null
    var yourLobbyChanged: ((LobbyItemModel) -> Unit)? = null
    var yourLobbyDeleted: (() -> Unit)? = null

    fun sendInviteAnswer(answer: InviteAnswer) {
        hub.invoke("AnswerToInvite", answer)
    }


}
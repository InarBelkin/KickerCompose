package com.inar.kickercompose.data.viemodels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.IsAccepted
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import com.inar.kickercompose.data.models.lobby.Role
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.net.repositories.ILobbyMessagesRepository
import com.inar.kickercompose.data.net.repositories.ILobbyRepository
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.services.ServiceUtil
import com.inar.kickercompose.ui.navigation.NavigationItems
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattlePart @Inject constructor(
    private val account: AccountHandler,
    private val lobby: ILobbyRepository,
    private val lobbyMessages: ILobbyMessagesRepository,
) {
    private lateinit var receiver: BroadcastReceiver

    private val _delegateLobby = LoadedState.DelegateLiveData<List<LobbyItemModel>>(emptyList())
    val lobbyLd by _delegateLobby


    private val _delegateMyLobby = LoadedState.DelegateLiveData<LobbyItemModel?>(null)
    val myLobbyLd by _delegateMyLobby

    suspend fun loadLobby() {
        _delegateLobby.reLoad { lobby.getLobbys() }
    }

    suspend fun loadMyLobby() {
        _delegateMyLobby.reLoad { lobby.getMyLobby() }
    }

    suspend fun startBattle(): MessageBase = lobby.createLobby(LobbyItemModel().apply {
        message = "just battle"
        initiator = LobbyUserShortInfo().apply {
            id = account.getUserClaims()?.id
        }
        sideA = listOf(LobbyUserShortInfo().apply {
            role = Role.Attack.num;accepted = IsAccepted.Empty.a
        }, LobbyUserShortInfo().apply {
            role = Role.Defense.num;accepted = IsAccepted.Empty.a
        })
        sideB = listOf(LobbyUserShortInfo().apply {
            role = Role.Attack.num;accepted = IsAccepted.Empty.a
        }, LobbyUserShortInfo().apply {
            role = Role.Defense.num;accepted = IsAccepted.Empty.a
        })
    })

    suspend fun updateBattle(lobbyModel: LobbyItemModel): MessageBase =
        lobby.updateLobby(lobbyModel)


    suspend fun inviteOne(invitedId: String, message: InviteMessage): MessageBase {
        val l = myLobbyLd.value!!.value!!
        val item = if (message.side == 0) l.sideA[message.position] else l.sideB[message.position]

        item.id = invitedId
        item.role = message.position
        item.accepted = IsAccepted.Invited.a

        val ret = updateBattle(l);
        if (ret.success) lobbyMessages.inviteOne(invitedId, message)

        return ret
    }

    suspend fun stopBattle(myId: String) {
        lobby.deleteLobby(myId)
    }

    suspend fun sendInviteAnswer(invite: InviteMessage, accept: Boolean) {
        val answer = InviteAnswer().also {
            it.invitedId = invite.invitedId
            it.initiatorId = invite.senderId
            it.accepted = accept
            it.side = invite.side
            it.position = invite.position
        }
        lobbyMessages.answerToInvite(answer)

    }

    fun observeLobbyChanges(context: Context) {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val lobbyModel =
                    intent?.getParcelableExtra<LobbyItemModel>(ServiceUtil.LobbyObserver.LOBBY_MODEL_EXTRA)
                if (lobbyModel != null) {
                    _delegateMyLobby.justChange(lobbyModel)
                }
            }
        }
        val intentFilter = IntentFilter(ServiceUtil.LobbyObserver.BROADCAST_ACTION)
        context.registerReceiver(receiver, intentFilter)
    }

    fun disposeObserveLobbyChanges(context: Context) {
        if (this::receiver.isInitialized) {
            context.unregisterReceiver(receiver)
        }
    }
}
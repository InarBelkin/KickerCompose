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
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.net.repositories.ILobbyMessagesRepository
import com.inar.kickercompose.data.net.repositories.ILobbyRepository
import com.inar.kickercompose.services.ServiceUtil
import retrofit2.HttpException
import java.lang.Exception
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


    private val _delegateMyLobby = LoadedState.DelegateLiveData<LobbyItemModel?>(null) { myLobby ->
        if (myLobby.value != null && myLobby is LoadedState.Success && lobbyLd.value is LoadedState.Success) {
            val rez =
                listOf(*lobbyLd.value!!.value.filter { it.initiator.id != myLobby.value?.initiator?.id }
                    .toTypedArray(), myLobby.value!!)
            _delegateLobby.justChange(rez)
        }
    }

    val myLobbyLd by _delegateMyLobby

    suspend fun loadLobby() {
        _delegateLobby.reLoad { lobby.getLobbys() }
    }

    suspend fun loadMyLobby() {
        _delegateMyLobby.reLoad { lobby.getMyLobby() }
    }

    suspend fun startBattle(isTwoPlayers: Boolean): MessageBase =
        lobby.createLobby(LobbyItemModel().apply {
            message = "just battle"
            initiator = LobbyUserShortInfo().apply {
                id = account.getUserClaims()?.id
            }

            fun addUsers(): List<LobbyUserShortInfo> = if (isTwoPlayers) listOf(
                LobbyUserShortInfo().apply {
                    role = Role.Attack.num;accepted = IsAccepted.Empty.a
                }, LobbyUserShortInfo().apply {
                    role = Role.Defense.num;accepted = IsAccepted.Empty.a
                })
            else listOf(LobbyUserShortInfo().apply {
                role = Role.Attack.num;accepted = IsAccepted.Empty.a
            })

            sideA = addUsers()
            sideB = addUsers()
        })

    suspend fun updateBattle(lobbyModel: LobbyItemModel): MessageBase =
        lobby.updateLobby(lobbyModel)

    suspend fun createAndInvite(invitedId: String, message: String): MessageBase {
        if (myLobbyLd.value?.value != null) return MessageBase(true, "")
        try {
            val claims = account.getUserClaims()!!
            val creatingLobby = LobbyItemModel().also {
                it.message = message

                it.initiator = LobbyUserShortInfo().apply {
                    id = claims.id
                    role = Role.Attack.num
                    accepted = IsAccepted.Accepted.a
                }

                it.sideA = listOf(
                    it.initiator
                )

                it.sideB = listOf(
                    LobbyUserShortInfo().apply {
                        id = invitedId
                        role = Role.Attack.num
                        accepted = IsAccepted.Invited.a
                    }
                )
            }

            val isCreated = lobby.createLobby(creatingLobby);
            if (isCreated.success) {
                val inviteMessage = InviteMessage().also {
                    it.senderId = claims.id
                    it.senderName = claims.name
                    it.invitedId = invitedId
                    it.message = message
                    it.side = 1
                    it.position = 0
                }
                lobbyMessages.inviteOne(invitedId, inviteMessage)
            }
            return MessageBase(true, "Success!")
        } catch (e: HttpException) {
            return MessageBase(false, e.message())
        } catch (e: Exception) {
            return MessageBase(false, e.message ?: "error")
        }
    }


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

    suspend fun inviteAll(side: Int, position: Int) {
        val l = myLobbyLd.value!!.value!!
        val item = if (side == 0) l.sideA[position]
        else l.sideB[position]

        item.id = null
        item.accepted = IsAccepted.AllInvited.a
        val ret = updateBattle(l);
        if (ret.success) {
            val message = InviteMessage().also {
                it.senderId = account.getUserClaims()?.id.toString()
                it.senderName = account.getUserClaims()?.name.toString()
                it.invitedId = "00000000-0000-0000-0000-000000000000"
                it.message = l.message
                it.side = side
                it.position = position
                it.isInviteToAll = true
            }
            lobbyMessages.inviteAll(message)
        }
    }


    suspend fun sendInviteAnswerFromGuest(invite: InviteMessage, accept: Boolean) {
        val answer = InviteAnswer().also {
            it.invitedId = invite.invitedId
            it.initiatorId = invite.senderId
            it.accepted = accept
            it.side = invite.side
            it.position = invite.position
        }
        lobbyMessages.answerToInvite(answer)
    }

    suspend fun sendInviteAnswerFromGuest(answer: InviteAnswer) {
        lobbyMessages.answerToInvite(answer)
    }

    suspend fun kickUser(side: Int, position: Int): MessageBase {
        val l = myLobbyLd.value!!.value!!
        val item = if (side == 0) l.sideA[position]
        else l.sideB[position]
        item.accepted = IsAccepted.Kicked.a
        return updateBattle(l)
    }

    suspend fun stopBattle(myId: String) {
        lobby.deleteLobby(myId)
    }

    suspend fun leaveBattle(dto: LeaveBattleDto) {
        lobbyMessages.leaveBattle(dto)
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
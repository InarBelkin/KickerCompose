package com.inar.kickercompose.data.viemodels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.inar.kickercompose.data.models.answers.BattleAnswerMessage
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.BattleStatus
import com.inar.kickercompose.data.models.lobby.IsAccepted
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import com.inar.kickercompose.data.models.lobby.Role
import com.inar.kickercompose.data.models.lobby.item.LobbyTimeStamp
import com.inar.kickercompose.data.models.lobby.item.ResultDto
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.states.message.messageBaseWrapper
import com.inar.kickercompose.data.net.repositories.interfaces.ILobbyMessagesRepository
import com.inar.kickercompose.data.net.repositories.interfaces.ILobbyRepository
import com.inar.kickercompose.services.ServiceUtil
import retrofit2.HttpException
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattlePart @Inject constructor(
    private val account: AccountHandler,
    private val lobby: ILobbyRepository,
    private val lobbyMessages: ILobbyMessagesRepository,
) {
    private lateinit var receiver: BroadcastReceiver
    private lateinit var deleteReceiver: BroadcastReceiver

    private val _delegateLobby = LoadedState.DelegateLiveData<List<LobbyItemModel>>(emptyList())
    val lobbyLd by _delegateLobby


    private val _delegateMyLobby = LoadedState.DelegateLiveData<LobbyItemModel?>(null) { myLobby ->
        if (myLobby.value != null && myLobby is LoadedState.Success && lobbyLd.value is LoadedState.Success) {
            val rez =
                listOf(*lobbyLd.value!!.value.filter { it.initiator.id != myLobby.value?.initiator?.id }
                    .toTypedArray(), myLobby.value)
            _delegateLobby.justChange(rez)
        }
    }

    val myLobbyLd by _delegateMyLobby

    suspend fun loadLobby() {
        _delegateLobby.reLoad { lobby.getLobbys() }
    }

    suspend fun loadMyLobby() {
        try {
            _delegateMyLobby.reLoad { lobby.getMyLobby() }
        } catch (e: HttpException) {
            _delegateMyLobby.justChange(null)
        }
    }

    suspend fun createBattle(isTwoPlayers: Boolean): MessageBase = messageBaseWrapper {
        lobby.createLobby(LobbyItemModel().apply {
            message = "just battle"
            timeStamps =
                arrayListOf(LobbyTimeStamp(BattleStatus.Created.num,
                    LocalDateTime.now(ZoneOffset.UTC),
                    0.0))
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

    }


    suspend fun updateBattle(lobbyModel: LobbyItemModel): MessageBase =
        lobby.updateLobby(lobbyModel)

    suspend fun createAndInvite(invitedId: String, message: String): MessageBase =
        messageBaseWrapper {
            if (myLobbyLd.value?.value != null) return MessageBase(true, "")

            val claims = account.getUserClaims()!!
            val creatingLobby = LobbyItemModel().also {
                it.message = message
                it.timeStamps =
                    arrayListOf(LobbyTimeStamp(BattleStatus.Created.num,
                        LocalDateTime.now(ZoneOffset.UTC),
                        0.0))

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
        }

    suspend fun checkStartBattle(): MessageBase {
        val l = myLobbyLd.value?.value ?: return MessageBase(false, "lobby doen't exists");

        val list = listOf(l.sideA, l.sideB).flatten()
        if (list.all { it.id != null && it.accepted == IsAccepted.Accepted.a }) return MessageBase(
            true,
            "ok, let's start")

        return MessageBase(false,
            "Your lobby is not full! You can start battle, but results will not be included in statistics")
    }

    suspend fun startBattle(): MessageBase = messageBaseWrapper {
        val l = myLobbyLd.value?.value!!;
        l.timeStamps.add(LobbyTimeStamp(BattleStatus.Started.num,
            LocalDateTime.now(ZoneOffset.UTC),
            0.0))

        updateBattle(l)
    }

    suspend fun pauseBattle(): MessageBase = messageBaseWrapper {
        val l = myLobbyLd.value?.value!!;
        val dif =
            Duration.between(l.timeStamps.last().globalTime, LocalDateTime.now(ZoneOffset.UTC))

        val battleTime = dif.seconds + l.timeStamps.last().battleTime

        l.timeStamps.add(LobbyTimeStamp(
            BattleStatus.Paused.num,
            LocalDateTime.now(ZoneOffset.UTC),
            battleTime))

        updateBattle(l)
    }


    suspend fun resumeBattle() = messageBaseWrapper {
        val l = myLobbyLd.value?.value!!;
        l.timeStamps.add(LobbyTimeStamp(BattleStatus.Started.num,
            LocalDateTime.now(ZoneOffset.UTC),
            l.lastTimeStamp!!.battleTime))
        updateBattle(l)
    }

    suspend fun startEnterResultsBattle() = messageBaseWrapper {
        val l = myLobbyLd.value?.value!!

        val battleTime: Double = if (l.lastTimeStamp!!.battleState == BattleStatus.Started.num) {
            val dif =
                Duration.between(LocalDateTime.now(ZoneOffset.UTC), l.timeStamps.last().globalTime)
            dif.seconds + l.timeStamps.last().battleTime
        } else l.lastTimeStamp!!.battleTime


        l.timeStamps.add(LobbyTimeStamp(BattleStatus.EnteringResults.num,
            LocalDateTime.now(ZoneOffset.UTC), battleTime))

        updateBattle(l)
    }

    suspend fun sendBattleResults(result: ResultDto) = messageBaseWrapper {
        val l = myLobbyLd.value?.value!!
        l.result = result
        updateBattle(l)
    }

    suspend fun endBattle(): BattleAnswerMessage {
        if (myLobbyLd.value!!.value?.result?.isWinnerA == null) {
            return BattleAnswerMessage().apply {
                success = false; message = "winner must be selected"
            }
        }
        return lobbyMessages.endBattle(myLobbyLd.value!!.value!!)
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

    suspend fun earlyEndBattle(myId: String) {
        lobbyMessages.earlyEndBattle(myId)
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
        try {
//            if (this::receiver.isInitialized) {
//                context.unregisterReceiver(receiver)
//            }
        } catch (e: IllegalArgumentException) {

        }
    }

    fun observeLobbyDeleted(context: Context, action: (Boolean, String) -> Unit) {
        deleteReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val withResult =
                    intent.getBooleanExtra(ServiceUtil.LobbyDeleted.WITH_RESULTS, false)
                val battleId = intent.getStringExtra(ServiceUtil.LobbyDeleted.BATTLE_ID)!!
                action(withResult, battleId);
            }
        }
        val intentFilter = IntentFilter(ServiceUtil.LobbyDeleted.BROADCAST_ACTION)
        context.registerReceiver(deleteReceiver, intentFilter)
    }

    fun disposeObserveDeleted(context: Context) {
        try {
            if (this::deleteReceiver.isInitialized) {
                context.unregisterReceiver(deleteReceiver)
            }
        } catch (e: IllegalArgumentException) {
        }
    }


}
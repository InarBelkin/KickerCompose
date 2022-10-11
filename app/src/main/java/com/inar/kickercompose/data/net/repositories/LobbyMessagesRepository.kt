package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.answers.BattleAnswerMessage
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.net.repositories.interfaces.ILobbyMessagesRepository
import retrofit2.http.Path
import javax.inject.Inject

class LobbyMessagesRepository @Inject constructor(private val networkService: NetworkService) :
    ILobbyMessagesRepository {
    override suspend fun inviteOne(invitedId: String, dto: InviteMessage) {
        networkService.lobbyMessages.inviteOne(invitedId, dto)
    }

    override suspend fun inviteAll(dto: InviteMessage) {
        networkService.lobbyMessages.inviteAll(dto)
    }

    override suspend fun answerToInvite(dto: InviteAnswer) {
        networkService.lobbyMessages.answerToInvite(dto)
    }

    override suspend fun leaveBattle(dto: LeaveBattleDto): MessageBase {
        return networkService.lobbyMessages.leaveBattle(dto)
    }

    override suspend fun endBattle(dto: LobbyItemModel): BattleAnswerMessage {
        return networkService.lobbyMessages.endBattle(dto)
    }

    override suspend fun earlyEndBattle(id: String): MessageBase{
        return networkService.lobbyMessages.earlyEndBattle(id)
    }
}
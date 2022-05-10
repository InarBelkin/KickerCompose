package com.inar.kickercompose.data.net.repositories.interfaces

import com.inar.kickercompose.data.models.answers.BattleAnswerMessage
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto

interface ILobbyMessagesRepository {
    suspend fun inviteOne(invitedId: String, dto: InviteMessage)

    suspend fun inviteAll(dto: InviteMessage)

    suspend fun answerToInvite(dto: InviteAnswer)
    suspend fun leaveBattle(dto: LeaveBattleDto): MessageBase
    suspend fun endBattle(dto: LobbyItemModel): BattleAnswerMessage
}
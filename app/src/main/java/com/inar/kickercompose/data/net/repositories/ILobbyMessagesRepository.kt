package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage

interface ILobbyMessagesRepository {
    suspend fun inviteOne(invitedId: String, dto: InviteMessage)

    suspend fun inviteAll(dto: InviteMessage)

    suspend fun answerToInvite(dto: InviteAnswer)
}
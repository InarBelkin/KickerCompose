package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LobbyMessagesApi {

    @POST(Endpoints.lobbyMessagesApi + "inviteOne/{id}")
    suspend fun inviteOne(@Path(value = "id") invitedId: String, @Body dto: InviteMessage)

    @POST(Endpoints.lobbyMessagesApi + "inviteAll")
    suspend fun inviteAll(@Body dto: InviteMessage)

    @POST(Endpoints.lobbyMessagesApi + "answerToInvite")
    suspend fun answerToInvite(@Body dto: InviteAnswer)

    @POST(Endpoints.lobbyMessagesApi + "leave")
    suspend fun leaveBattle(@Body dto: LeaveBattleDto): MessageBase
}
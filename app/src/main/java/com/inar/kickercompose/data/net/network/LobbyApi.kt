package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.account.RegisterAnswerDto
import com.inar.kickercompose.data.models.account.RegisterDto
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.net.Endpoints
import retrofit2.http.Body
import retrofit2.http.POST

interface LobbyApi {
    @POST(Endpoints.lobbyApi + "invite")
    suspend fun inviteOne(@Body dto: InviteRequestDto): Unit
}
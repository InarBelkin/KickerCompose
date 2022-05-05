package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.net.Endpoints
import retrofit2.http.*

interface LobbyApi {
    @POST(Endpoints.lobbyApi + "invite")
    suspend fun inviteOne(@Body dto: InviteRequestDto): Unit

    @GET("api/Lobby")
    suspend fun getLobbys(@Header("Authorization") authHeader: String): ArrayList<LobbyItemModel>

    @GET("api/Lobby/lobby")
    suspend fun getMyLobby(@Header("Authorization") authHeader: String): LobbyItemModel?

    @POST("api/Lobby")
    suspend fun createLobby(@Body dto: LobbyItemModel): MessageBase

    @PUT("api/Lobby")
    suspend fun updateLobby(@Body dto: LobbyItemModel): MessageBase

    @DELETE("api/Lobby/{id}")
    suspend fun deleteLobby(@Path(value = "id") userId: String): MessageBase
}
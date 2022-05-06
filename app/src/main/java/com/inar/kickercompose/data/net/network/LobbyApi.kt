package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.ui.navigation.NavigationItems
import retrofit2.http.*

interface LobbyApi {
    @GET("api/Lobby")
    suspend fun getLobbys(@Header("Authorization") authHeader: String): ArrayList<LobbyItemModel>

    @GET("api/Lobby/mylobby")
    suspend fun getMyLobby(@Header("Authorization") authHeader: String): LobbyItemModel?

    @GET(Endpoints.lobbyApi + "lobby/{id}")
    suspend fun getLobby(@Path(value = "id") initiatorId: String): LobbyItemModel

    @POST("api/Lobby")
    suspend fun createLobby(@Body dto: LobbyItemModel): MessageBase

    @PUT("api/Lobby")
    suspend fun updateLobby(@Body dto: LobbyItemModel): MessageBase

    @DELETE("api/Lobby/{id}")
    suspend fun deleteLobby(@Path(value = "id") userId: String): MessageBase
}
package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import retrofit2.http.GET
import retrofit2.http.Path

interface BattleApi {

    @GET(Endpoints.battle + "{id}")
    suspend fun getBattle(@Path(value = "id") id: String): LobbyItemModel
}
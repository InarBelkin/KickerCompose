package com.inar.kickercompose.data.net.repositories.interfaces

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState

interface ILobbyRepository {
    suspend fun getLobbys(): LoadedState<List<LobbyItemModel>>
    suspend fun getMyLobby(): LoadedState<LobbyItemModel?>

    suspend fun createLobby(dto: LobbyItemModel): MessageBase

    suspend fun updateLobby(dto: LobbyItemModel): MessageBase

    suspend fun deleteLobby(userId: String): MessageBase
    suspend fun getLobby(initiatorId: String): LoadedState<LobbyItemModel>

}
package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState

interface ILobbyRepository {
    suspend fun InviteOne(dto: InviteRequestDto)

    suspend fun getLobbys(): LoadedState<List<LobbyItemModel>>
    suspend fun getMyLobby(): LoadedState<LobbyItemModel?>

    suspend fun createLobby(dto: LobbyItemModel): MessageBase

    suspend fun updateLobby(dto: LobbyItemModel): MessageBase

    suspend fun deleteLobby(userId: String): MessageBase

}
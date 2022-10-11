package com.inar.kickercompose.data.net.repositories.interfaces

import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState

interface IBattleRepository {
    suspend fun getBattle(id: String): LoadedState<LobbyItemModel>
}
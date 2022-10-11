package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.states.loadstates.loadWrapper
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.net.repositories.interfaces.ILobbyRepository
import com.inar.kickercompose.data.viemodels.AccountHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LobbyRepository @Inject constructor(
    private val networkService: NetworkService,
    private val accountHandler: AccountHandler,
) : ILobbyRepository {

    override suspend fun getLobbys(): LoadedState<List<LobbyItemModel>> {
        return loadWrapper {
            networkService.lobby.getLobbys("Bearer " + accountHandler.getAccessToken())
        }
    }

    override suspend fun getMyLobby(): LoadedState<LobbyItemModel?> {
        return loadWrapper {
            try {
                networkService.lobby.getMyLobby("Bearer " + accountHandler.getAccessToken())
            } catch (e: KotlinNullPointerException) {   //this is not error, this is dumb
                null
            }
        }
    }

    override suspend fun getLobby(initiatorId: String): LoadedState<LobbyItemModel> {
        return loadWrapper { networkService.lobby.getLobby(initiatorId) }
    }

    override suspend fun createLobby(dto: LobbyItemModel): MessageBase {
        return networkService.lobby.createLobby(dto)
    }

    override suspend fun updateLobby(dto: LobbyItemModel): MessageBase {
        return networkService.lobby.updateLobby(dto)
    }

    override suspend fun deleteLobby(userId: String): MessageBase {
        return networkService.lobby.deleteLobby(userId)
    }

    companion object {
        const val TAG = "lobbyRepository"
    }
}
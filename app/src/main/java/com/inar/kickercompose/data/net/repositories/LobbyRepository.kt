package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.viemodels.AccountHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LobbyRepository @Inject constructor(
    private val networkService: NetworkService,
    private val accountHandler: AccountHandler,
) : ILobbyRepository {

    override suspend fun InviteOne(dto: InviteRequestDto) { //TODO: where is try catch?
        networkService.lobby.inviteOne(dto)
    }

    override suspend fun getLobbys(): LoadedState<List<LobbyItemModel>> {
        return loadWrapper { networkService.lobby.getLobbys("Bearer " + accountHandler.getAccessToken()) }
    }

    override suspend fun getMyLobby(): LoadedState<LobbyItemModel?> {
        return loadWrapper { networkService.lobby.getMyLobby("Bearer " + accountHandler.getAccessToken()) }
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


}
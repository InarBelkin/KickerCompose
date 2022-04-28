package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.viemodels.AccountHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LobbyRepository @Inject constructor(
    private val networkService: NetworkService,
) : ILobbyRepository {

    override suspend fun InviteOne(dto: InviteRequestDto) {
        networkService.lobby.inviteOne(dto)
    }
}
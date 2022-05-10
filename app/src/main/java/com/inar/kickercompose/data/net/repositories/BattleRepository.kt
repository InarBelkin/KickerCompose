package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.states.loadstates.loadWrapper
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.net.repositories.interfaces.IBattleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BattleRepository @Inject constructor(private val networkService: NetworkService) :
    IBattleRepository {

    override suspend fun getBattle(id: String) = loadWrapper {
        networkService.battle.getBattle(id)
    }
}
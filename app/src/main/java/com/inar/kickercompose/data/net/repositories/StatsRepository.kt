package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.states.loadstates.loadWrapper
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.net.repositories.interfaces.IStatsRepository
import com.inar.kickercompose.data.viemodels.AccountHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val networkService: NetworkService,
    private val accountHandler: AccountHandler,

    ) : IStatsRepository {
    override suspend fun getUserDetails(id: String): LoadedState<UserDetails> =
        loadWrapper {
            networkService.stats.getUserDetails(id,
                "Bearer " + accountHandler.getAccessToken())
        }

    override suspend fun getLeaderboard(): LoadedState<LeaderboardWrapper> =
        loadWrapper {
            val lwrapper = networkService.stats.getLeaderboard()
            lwrapper.data = lwrapper.data.sortedByDescending {
                it.elo
            }.toList()
            for (i in 0 until lwrapper.data.count()) {
                lwrapper.data[i].stPlace = i + 1
            }
            lwrapper
        }

    suspend fun getMe(): LoadedState<UserDetails> = loadWrapper {
        return loadWrapper { networkService.stats.getMe("Bearer " + accountHandler.getAccessToken()) }
    }
}


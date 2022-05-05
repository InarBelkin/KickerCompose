package com.inar.kickercompose.data.net.repositories

import android.util.Log
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.states.loadstates.loadWrapper
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.viemodels.AccountHandler
import java.lang.Exception
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
        loadWrapper { networkService.stats.getLeaderboard() }

    suspend fun getMe(): LoadedState<UserDetails> = loadWrapper {
        return loadWrapper { networkService.stats.getMe("Bearer " + accountHandler.getAccessToken()) }
    }
}


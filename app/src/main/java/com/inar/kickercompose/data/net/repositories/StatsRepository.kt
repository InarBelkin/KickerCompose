package com.inar.kickercompose.data.net.repositories

import android.util.Log
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.NetworkService
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val networkService: NetworkService,
) : IStatsRepository {
    override suspend fun getUserDetails(id: String): LoadedState<UserDetails> =
        loadWrapper { networkService.stats.getUserDetails(id) }

    override suspend fun getLeaderboard(): LoadedState<LeaderboardWrapper> =
        loadWrapper { networkService.stats.getLeaderboard() }

    suspend fun getMe(): LoadedState<UserDetails> = loadWrapper {
        return loadWrapper { networkService.stats.getMe() }
    }
}

inline fun <reified T> loadWrapper(loader: () -> T): LoadedState<T> {
    try {
        return LoadedState.Success(loader.invoke());
    } catch (e: Exception) {
        Log.e("repos", e.message ?: "")
        val type = T::class.java
        return LoadedState.Error(type.newInstance(), e)
    }
}

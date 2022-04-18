package com.inar.kickercompose.data.net.repositories

import android.util.Log
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.other.loadstates.LoadedState
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.NetworkService
import java.lang.Exception
import javax.inject.Inject
import kotlin.reflect.KClass

class StatsRepository @Inject constructor() : IStatsRepository {
    override suspend fun getUserDetails(id: String): LoadedState<UserDetails> =
        loadWrapper { NetworkService.stats.getUserDetails(id) }

    override suspend fun getLeaderboard(): LoadedState<LeaderboardWrapper> =
        loadWrapper { NetworkService.stats.getLeaderboard() }

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

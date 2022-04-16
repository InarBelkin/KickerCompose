package com.inar.kickercompose.data.net.repositories

import android.util.Log
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.LoadedState
import com.inar.kickercompose.data.models.UserDetails
import com.inar.kickercompose.data.net.NetworkService
import kotlinx.coroutines.delay
import java.lang.Exception
import javax.inject.Inject

class StatsRepository @Inject constructor() : IStatsRepository {
    override suspend fun getUserDetails(id: String): UserDetails {
        try {
            return NetworkService.stats.getUserDetails(id)
        } catch (e: Exception) {
            Log.e("repos", e.message ?: "")
        }
        return UserDetails()
    }

    override suspend fun getLeaderboard(): LoadedState<LeaderboardWrapper> {
        try {
            //delay(1000)
            return LoadedState.Success(NetworkService.stats.getLeaderboard())
        } catch (e: Exception) {
            Log.e("repos", e.message ?: "")
            return LoadedState.Error(value = LeaderboardWrapper(), error = e)
        }
    }


}
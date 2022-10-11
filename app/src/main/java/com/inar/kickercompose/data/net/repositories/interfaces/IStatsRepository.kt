package com.inar.kickercompose.data.net.repositories.interfaces

import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.userdetails.UserDetails

interface IStatsRepository {
    suspend fun getUserDetails(id: String): LoadedState<UserDetails>
    suspend fun getLeaderboard(): LoadedState<LeaderboardWrapper>
}
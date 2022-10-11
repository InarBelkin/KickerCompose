package com.inar.kickercompose.data.viemodels

import android.util.Log
import androidx.lifecycle.*
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.repositories.interfaces.ILobbyRepository
import com.inar.kickercompose.data.net.repositories.interfaces.IStatsRepository
import com.inar.kickercompose.data.net.signal.HubHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: IStatsRepository,
    private val lobby: ILobbyRepository,
    val account: AccountHandler,
    val hub: HubHandler,
    val battle: BattlePart,
) : ViewModel() {


    init {
        Log.d("viewmodel", "created")
    }

    private val _delegateDetails = LoadedState.DelegateLiveData(UserDetails())
    val userDetailsLiveData by _delegateDetails

    suspend fun loadUserDetails(userId: String) {
        _delegateDetails.reLoad { repository.getUserDetails(userId) }
    }


    private val _delegateLeaderboard = LoadedState.DelegateLiveData(LeaderboardWrapper())
    val leaderboardLd by _delegateLeaderboard

    suspend fun loadLeaderboard() {
        _delegateLeaderboard.reLoad { repository.getLeaderboard() }
    }


}


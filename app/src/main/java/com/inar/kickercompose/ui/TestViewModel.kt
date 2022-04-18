package com.inar.kickercompose.ui

import android.util.Log
import androidx.lifecycle.*
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.other.loadstates.LoadedState
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.net.repositories.IStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: IStatsRepository
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


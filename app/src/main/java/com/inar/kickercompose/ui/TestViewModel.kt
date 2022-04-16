package com.inar.kickercompose.ui

import android.util.Log
import androidx.lifecycle.*
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.LoadedState
import com.inar.kickercompose.data.models.UserDetails
import com.inar.kickercompose.data.net.repositories.IStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: IStatsRepository
) : ViewModel() {

    init {
        Log.d("viewmodel", "created")
    }

    val userDetailsLiveData: LiveData<UserDetails>
        get() = _userDetailsLd
    private val _userDetailsLd = MutableLiveData(UserDetails())

    suspend fun loadUserDetails(userId: String) {
        _userDetailsLd.value = repository.getUserDetails(userId)
    }

    val leaderboardLd: LiveData<LoadedState<LeaderboardWrapper>>
        get() = _leaderboardLd
    private val _leaderboardLd =
        MutableLiveData<LoadedState<LeaderboardWrapper>>(LoadedState.Loading(LeaderboardWrapper()))

    suspend fun loadLeaderboard() {
        _leaderboardLd.value = LoadedState.Loading(_leaderboardLd.value!!.value)
        when (val v = repository.getLeaderboard()) {
            is LoadedState.Success -> _leaderboardLd.value = v
            is LoadedState.Error -> _leaderboardLd.value = LoadedState.Error(
                (_leaderboardLd.value as LoadedState.Loading<LeaderboardWrapper>).value,
                v.error
            )
            else -> {}
        }
    }
}
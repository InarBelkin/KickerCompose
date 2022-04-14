package com.inar.kickercompose.ui

import androidx.lifecycle.*
import com.inar.kickercompose.data.models.UserDetails
import com.inar.kickercompose.data.net.repositories.IStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: IStatsRepository
) : ViewModel() {

    //private val repository = StatsRepository();

    val counterLiveData: LiveData<Int>
        get() = counter

    private val counter = MutableLiveData<Int>(0);


    fun increaseCounter() {
        counter.value = counter.value?.plus(1);
    }


    val userDetailsLiveData: LiveData<UserDetails>
        get() = _userDetailsLd

    private val _userDetailsLd = MutableLiveData(UserDetails());

    suspend fun loadUserDetails(userId: String) {
        _userDetailsLd.value = repository.getUserDetails(userId);
    }

}
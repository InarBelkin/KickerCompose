package com.inar.kickercompose.ui

import androidx.lifecycle.*
import com.inar.kickercompose.data.models.UserDetails
import com.inar.kickercompose.data.net.StatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TestViewModel : ViewModel(

) {

    private val repository = StatsRepository();

    val counterLiveData: LiveData<Int>
        get() = counter

    private val counter = MutableLiveData<Int>(0);


    fun increaseCounter() {
        counter.value = counter.value?.plus(1);
    }


    val UserDetailLiveData: LiveData<UserDetails>
        get() = userDetailsLd

    private val userDetailsLd = MutableLiveData(UserDetails());

    fun loadUserDetails(userId: String): Job =
        viewModelScope.launch {
            userDetailsLd.value = repository.getUserDetails(userId);
        }
}
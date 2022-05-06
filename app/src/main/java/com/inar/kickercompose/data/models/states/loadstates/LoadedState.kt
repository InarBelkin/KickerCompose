package com.inar.kickercompose.data.models.states.loadstates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception
import kotlin.reflect.KProperty

sealed class LoadedState<T>(val value: T) {

    class Loading<T>(value: T) : LoadedState<T>(value)

    class Success<T>(value: T) : LoadedState<T>(value)

    class Error<T>(value: T, val error: Exception) : LoadedState<T>(value)

    companion object {
        inline fun <T> loadJuggler(
            ld: MutableLiveData<LoadedState<T>>,
            loadingFun: () -> LoadedState<T>,
        ): LoadedState<T> {
            ld.value = Loading(ld.value!!.value)
            when (val v = loadingFun.invoke()) {
                is Success -> ld.value = v
                is Error -> ld.value = Error(ld.value!!.value, v.error)
                else -> {}
            }
            return ld.value!!
        }
    }

    class DelegateLiveData<T>(
        initValue: T,
        private val interceptor: (LoadedState<T>) -> Unit = {},
    ) {
        private val _mld = MutableLiveData<LoadedState<T>>(Loading(initValue))
        operator fun getValue(thisRef: Any?, property: KProperty<*>): LiveData<LoadedState<T>> {
            return _mld;
        }

        suspend fun reLoad(loadingFun: suspend () -> LoadedState<T>) {
            val rez = loadJuggler(_mld) { loadingFun.invoke() }
            interceptor(rez)
        }

        fun justChange(value: T) {
            _mld.value = LoadedState.Success(value)
            interceptor(LoadedState.Success(value))
        }
    }

}
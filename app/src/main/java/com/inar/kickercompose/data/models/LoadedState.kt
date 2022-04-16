package com.inar.kickercompose.data.models

import java.lang.Exception

sealed class LoadedState<T>(val value: T) {

    class Loading<T>(value: T) : LoadedState<T>(value)

    class Success<T>(value: T) : LoadedState<T>(value)

    class Error<T>(value: T, val error: Exception) : LoadedState<T>(value)
}
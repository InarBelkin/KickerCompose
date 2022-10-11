package com.inar.kickercompose.data.models.states.loadstates

import android.util.Log
import java.lang.Exception

inline fun <reified T> loadWrapper(loader: () -> T): LoadedState<T> {
    try {
        return LoadedState.Success(loader.invoke());
    } catch (e: Exception) {
        Log.e("repos", e.message ?: "")

        val type = T::class.java
        if (type.isInterface) {
            if (type.name == "java.util.List" )
                return LoadedState.Error(emptyList<Any>() as T, e)
        }
        return LoadedState.Error(type.newInstance(), e)
    }
}

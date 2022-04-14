package com.inar.kickercompose.data.net

import android.util.Log
import com.inar.kickercompose.data.models.UserDetails
import retrofit2.Call
import java.lang.Exception

class StatsRepository {
    suspend fun getUserDetails(id: String): UserDetails {
        try {
            return NetworkService.stats.getUserDetails(id)
        } catch (e: Exception) {
            Log.e("repos", e.message ?: "")
        }
        return UserDetails()
    }


}
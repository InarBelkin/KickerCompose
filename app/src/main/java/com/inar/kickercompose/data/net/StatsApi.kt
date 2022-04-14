package com.inar.kickercompose.data.net

import com.inar.kickercompose.data.models.UserDetails
import com.inar.kickercompose.data.models.UserLeaderboard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StatsApi {
    @GET(Endpoints.stats)
    fun getLeaderboard(): Call<ArrayList<UserLeaderboard>>

    @GET("api/stats/userDetails/{id}")
    suspend fun getUserDetails(@Path(value = "id") userId: String): UserDetails


}
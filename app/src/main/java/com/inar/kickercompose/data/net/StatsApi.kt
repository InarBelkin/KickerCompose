package com.inar.kickercompose.data.net

import com.inar.kickercompose.data.models.UserLeaderboard
import retrofit2.Call
import retrofit2.http.GET

interface StatsApi {
    @GET(Endpoints.stats)
    fun getLeaderboard(): Call<ArrayList<UserLeaderboard>>
}
package com.inar.kickercompose.data.net

import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.userdetails.UserDetails
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StatsApi {
    @GET(Endpoints.stats)
    suspend fun getLeaderboard(): LeaderboardWrapper

    @GET("api/stats/userDetails/{id}")
    suspend fun getUserDetails(
        @Path(value = "id") userId: String,
        @Header("Authorization") authHeader: String,
    ): UserDetails

    @GET("api/stats/me")
    suspend fun getMe(@Header("Authorization") authHeader: String): UserDetails

}
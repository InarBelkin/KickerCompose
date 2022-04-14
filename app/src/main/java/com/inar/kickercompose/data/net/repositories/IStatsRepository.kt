package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.UserDetails

interface IStatsRepository {
    suspend fun getUserDetails(id: String): UserDetails
}
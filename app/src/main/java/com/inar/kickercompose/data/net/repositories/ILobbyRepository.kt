package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.lobby.InviteRequestDto

interface ILobbyRepository {
    suspend fun InviteOne(dto: InviteRequestDto)
}
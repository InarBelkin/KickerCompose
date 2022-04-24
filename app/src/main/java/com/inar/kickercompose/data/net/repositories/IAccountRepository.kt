package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.account.*

interface IAccountRepository {
    suspend fun mailRegistration(dto: RegisterDto): RegisterAnswerDto

    suspend fun loginMail(dto: LoginDto): LoginAnswerDto

    suspend fun refresh(dto: RefreshDto): LoginAnswerDto

    suspend fun logOut(refreshToken: String)
}
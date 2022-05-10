package com.inar.kickercompose.data.net.repositories.interfaces

import com.inar.kickercompose.data.models.account.*
import com.inar.kickercompose.data.models.answers.MessageBase

interface IAccountRepository {
    suspend fun mailRegistration(dto: RegisterDto): RegisterAnswerDto

    suspend fun loginMail(dto: LoginDto): LoginAnswerDto

    suspend fun refresh(dto: RefreshDto): LoginAnswerDto

    suspend fun logOut(accessToken: String, dto: LogoutDto): MessageBase
}
package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.account.*
import com.inar.kickercompose.data.net.NetworkService
import javax.inject.Inject

class AccountRepository @Inject constructor() : IAccountRepository {
    override suspend fun mailRegistration(dto: RegisterDto): RegisterAnswerDto =
        NetworkService.account.mailRegistration(dto)

    override suspend fun loginMail(dto: LoginDto): LoginAnswerDto =
        NetworkService.account.loginMail(dto)

    override suspend fun refresh(dto: RefreshDto): LoginAnswerDto =
        NetworkService.account.refresh(dto)

    override suspend fun logOut(refreshToken: String) {
            TODO()
    }

}
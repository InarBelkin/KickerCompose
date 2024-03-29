package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.models.account.*
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.net.repositories.interfaces.IAccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val networkService: NetworkService
) : IAccountRepository {
    override suspend fun mailRegistration(dto: RegisterDto): RegisterAnswerDto =
        networkService.account.mailRegistration(dto)

    override suspend fun loginMail(dto: LoginDto): LoginAnswerDto =
        networkService.account.loginMail(dto)

    override suspend fun refresh(dto: RefreshDto): LoginAnswerDto =
        networkService.account.refresh(dto)

    override suspend fun logOut(accessToken: String,dto: LogoutDto): MessageBase =
        networkService.account.logout("Bearer " + accessToken, dto)

}
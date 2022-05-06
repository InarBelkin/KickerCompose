package com.inar.kickercompose.data.net.network

import com.inar.kickercompose.data.models.account.*
import com.inar.kickercompose.data.models.answers.MessageBase
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {
    @POST(Endpoints.accountApi + "registration/mail")
    suspend fun mailRegistration(@Body dto: RegisterDto): RegisterAnswerDto

    @POST(Endpoints.accountApi + "login")
    suspend fun loginMail(@Body dto: LoginDto): LoginAnswerDto

    @POST(Endpoints.accountApi + "refresh")
    suspend fun refresh(@Body dto: RefreshDto): LoginAnswerDto

    @POST(Endpoints.accountApi + "logout")
    suspend fun logout(
        @Header("Authorization") authHeader: String,
        @Body dto: LogoutDto,
    ): MessageBase
}
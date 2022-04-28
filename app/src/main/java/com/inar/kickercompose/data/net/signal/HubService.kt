package com.inar.kickercompose.data.net.signal

import android.content.Context
import android.os.UserManager
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.net.repositories.ILobbyRepository
import com.inar.kickercompose.data.net.repositories.LobbyRepository
import com.inar.kickercompose.data.viemodels.AccountHandler
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.rxSingle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HubService @Inject constructor(
    private val account: AccountHandler,
    @ApplicationContext private val appContext: Context,
    private val lobby: ILobbyRepository,
) {
    lateinit var hub: HubConnection

    fun start(accessToken: String, callback: (String, String) -> Unit) {
        val url = appContext.getString(R.string.baseUrl) + "invitation"

        hub = HubConnectionBuilder.create(url)
            .withAccessTokenProvider(
                rxSingle {
                    account.getAccessToken() ?: ""
                }
            ).build()

        hub.on("InviteOne", { i, j ->
            callback(i, j)
        }, String::class.java, String::class.java)

        hub.start().subscribe() {
           // hub.invoke("Send", "hello Android")
        }

    }

    suspend fun inviteOne(dto: InviteRequestDto) {
        lobby.InviteOne(dto);
    }




}
package com.inar.kickercompose.data.net.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkService {
    private val mRetrofit: Retrofit;

    companion object {
        const val BaseUrl = "http://10.0.2.2:5093/";
    }

    init {
        val okHttpClient = OkHttpClient().newBuilder();
        mRetrofit = Retrofit.Builder().baseUrl(BaseUrl).client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create()).build();
    }

    val stats: StatsApi = mRetrofit.create(StatsApi::class.java);
    val account: AccountApi = mRetrofit.create(AccountApi::class.java)
    val lobby: LobbyApi = mRetrofit.create(LobbyApi::class.java)
    val lobbyMessages: LobbyMessagesApi = mRetrofit.create(LobbyMessagesApi::class.java)
}
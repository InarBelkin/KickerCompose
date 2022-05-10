package com.inar.kickercompose.data.net.network

import com.google.gson.GsonBuilder
import com.inar.kickercompose.data.models.adapters.LocalDateTimeDeserializer
import com.inar.kickercompose.data.models.adapters.LocalDateTimeSerializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime


class NetworkService {
    private val mRetrofit: Retrofit;

    companion object {
        const val BaseUrl = "http://10.0.2.2:5093/";
    }

    init {
        val gson =
            GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
                .create()

        val okHttpClient = OkHttpClient().newBuilder();
        mRetrofit = Retrofit.Builder().baseUrl(BaseUrl).client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    val stats: StatsApi = mRetrofit.create(StatsApi::class.java);
    val account: AccountApi = mRetrofit.create(AccountApi::class.java)
    val lobby: LobbyApi = mRetrofit.create(LobbyApi::class.java)
    val lobbyMessages: LobbyMessagesApi = mRetrofit.create(LobbyMessagesApi::class.java)
    val battle: BattleApi = mRetrofit.create(BattleApi::class.java)
}
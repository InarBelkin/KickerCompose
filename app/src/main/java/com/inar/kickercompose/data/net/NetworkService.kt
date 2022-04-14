package com.inar.kickercompose.data.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private val mRetrofit: Retrofit;
    const val BaseUrl = "http://10.0.2.2:5093/";

    init {
        val okHttpClient = OkHttpClient().newBuilder();
        mRetrofit = Retrofit.Builder().baseUrl(BaseUrl).client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create()).build();
    }

    val stats: StatsApi = mRetrofit.create(StatsApi::class.java);

}
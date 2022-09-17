package com.teamdefine.signease.api

import com.teamdefine.signease.BuildConfig
import com.teamdefine.signease.auth.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.hellosign.com/"

object RetrofitInstance {
    var loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(
        loggingInterceptor
    ).addInterceptor(BasicAuthInterceptor(BuildConfig.API_KEY, ""))

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: HelloSignAPI by lazy {
        retrofit.create(HelloSignAPI::class.java)
    }
}
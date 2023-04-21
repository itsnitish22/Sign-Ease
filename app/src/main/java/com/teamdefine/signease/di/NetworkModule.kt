package com.teamdefine.signease.di

import com.teamdefine.signease.BuildConfig
import com.teamdefine.signease.di.auth.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.hellosign.com/"

val networkModule = module {
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun provideClientBuilder() = OkHttpClient.Builder().addInterceptor(
        provideLoggingInterceptor()
    ).addInterceptor(
        BasicAuthInterceptor(
            BuildConfig.API_KEY,
            ""
        )
    )

    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideClientBuilder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    single { provideLoggingInterceptor() }
    single { provideClientBuilder() }
    single { provideRetrofit() }
}
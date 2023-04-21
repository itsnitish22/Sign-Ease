package com.teamdefine.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teamdefine.data.BuildConfig
import com.teamdefine.data.services.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://api.hellosign.com/"

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideClientBuilder(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(
            httpLoggingInterceptor
        ).addInterceptor(
            BasicAuthInterceptor(
                BuildConfig.API_KEY,
                ""
            )
        )
    }

    @Provides
    @Singleton
    @Named("auth_retrofit")
    fun provideRetrofit(
        okHttpBuilder: OkHttpClient.Builder,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(@Named("auth_retrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
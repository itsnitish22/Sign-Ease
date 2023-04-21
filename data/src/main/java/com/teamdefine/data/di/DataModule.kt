package com.teamdefine.data.di

import com.teamdefine.data.services.ApiRepositoryImpl
import com.teamdefine.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideApiRepository(apiRepositoryImpl: ApiRepositoryImpl): ApiRepository
}
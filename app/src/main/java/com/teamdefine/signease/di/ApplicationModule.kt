package com.teamdefine.signease.di

import com.teamdefine.data.services.ApiRepositoryImpl
import com.teamdefine.domain.repository.ApiRepository
import org.koin.dsl.module

val applicationModules = module(override = true) {
    factory<ApiRepository> { ApiRepositoryImpl() }
}
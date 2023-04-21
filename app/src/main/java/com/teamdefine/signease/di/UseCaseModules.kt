package com.teamdefine.signease.di

import com.teamdefine.domain.interactors.main.*
import org.koin.dsl.module

val useCaseModules = module {
    factory { GetAllSignatureRequestsUseCase() }
    factory { SendDocForSignatureUseCase() }
    factory { GetTemplatesUseCase() }
    factory { GetFileUrlUseCase() }
    factory { DeleteRequestUseCase() }
    factory { CreateAppUseCase() }
    factory { DeleteAppUseCase() }
}
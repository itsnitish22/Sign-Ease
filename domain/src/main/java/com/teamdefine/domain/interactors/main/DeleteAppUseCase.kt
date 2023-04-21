package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteAppUseCase : KoinComponent {
    private val apiRepository: ApiRepository by inject()
    suspend operator fun invoke(clientId: String) =
        apiRepository.deleteApp(clientId)
}
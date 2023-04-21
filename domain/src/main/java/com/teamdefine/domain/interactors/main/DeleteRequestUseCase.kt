package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteRequestUseCase : KoinComponent {
    private val apiRepository: ApiRepository by inject()

    suspend operator fun invoke(sign_id: String) {
        apiRepository.deleteRequest(sign_id)
    }
}
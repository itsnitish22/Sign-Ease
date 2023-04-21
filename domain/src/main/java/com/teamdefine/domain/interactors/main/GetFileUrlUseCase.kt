package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFileUrlUseCase : KoinComponent {
    private val apiRepository: ApiRepository by inject()

    suspend operator fun invoke(sign_id: String, get_url: Boolean) =
        apiRepository.getURL(sign_id, get_url)
}
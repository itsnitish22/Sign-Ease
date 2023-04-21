package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.models.post_create_app.CreateAPIApp
import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateAppUseCase : KoinComponent {
    private val apiRepository: ApiRepository by inject()
    suspend operator fun invoke(body: CreateAPIApp) = apiRepository.createApp(body)
}
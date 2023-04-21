package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.models.post_template_for_sign.Document
import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SendDocForSignatureUseCase : KoinComponent {
    private val apiRepository: ApiRepository by inject()

    suspend operator fun invoke(document: Document) = apiRepository.sendDocForSignature(document)
}
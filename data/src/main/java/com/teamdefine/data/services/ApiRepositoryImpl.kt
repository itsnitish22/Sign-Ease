package com.teamdefine.data.services

import com.teamdefine.domain.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.domain.models.get_all_templates.Templates
import com.teamdefine.domain.models.get_download_file.Download
import com.teamdefine.domain.models.post_create_app.CreateAPIApp
import com.teamdefine.domain.models.post_create_app.response.CreateAppResponse
import com.teamdefine.domain.models.post_template_for_sign.Document
import com.teamdefine.domain.models.post_template_for_sign.response.ResponseSign
import com.teamdefine.domain.repository.ApiRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiRepositoryImpl : ApiRepository, KoinComponent {
    private val apiService: ApiService by inject()

    override suspend fun getAllSignatureRequestsUse(): SignatureRequests {
        return apiService.getSignatureRequests()
    }

    override suspend fun sendDocForSignature(document: Document): ResponseSign {
        return apiService.sendDocForSignatures(document)
    }

    override suspend fun getTemplates(): Templates {
        return apiService.getTemplates()
    }

    override suspend fun getURL(sign_id: String, get_url: Boolean): Download {
        return apiService.getURL(sign_id, get_url)
    }

    override suspend fun deleteRequest(sign_id: String) {
        apiService.deleteRequest(sign_id)
    }

    override suspend fun createApp(body: CreateAPIApp): CreateAppResponse {
        return apiService.createApp(body)
    }

    override suspend fun deleteApp(clientId: String): retrofit2.Response<Unit> {
        return apiService.deleteApp(clientId)
    }
}
package com.teamdefine.domain.repository

import com.teamdefine.domain.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.domain.models.get_all_templates.Templates
import com.teamdefine.domain.models.get_download_file.Download
import com.teamdefine.domain.models.post_create_app.CreateAPIApp
import com.teamdefine.domain.models.post_create_app.response.CreateAppResponse
import com.teamdefine.domain.models.post_template_for_sign.Document
import com.teamdefine.domain.models.post_template_for_sign.response.ResponseSign

interface ApiRepository {
    suspend fun getAllSignatureRequestsUse(): SignatureRequests
    suspend fun sendDocForSignature(document: Document): ResponseSign
    suspend fun getTemplates(): Templates
    suspend fun getURL(sign_id: String, get_url: Boolean): Download
    suspend fun deleteRequest(sign_id: String): Unit
    suspend fun createApp(body: CreateAPIApp): CreateAppResponse
    suspend fun deleteApp(clientId: String): retrofit2.Response<Unit>
}
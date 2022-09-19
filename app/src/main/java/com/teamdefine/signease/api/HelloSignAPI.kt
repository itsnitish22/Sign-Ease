package com.teamdefine.signease.api

import com.teamdefine.signease.api.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.signease.api.models.get_all_templates.Templates
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.api.models.post_template_for_sign.response.ResponseSign
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface HelloSignAPI {
    @Headers("Content-Type: application/json")
    @POST("/v3/signature_request/send_with_template")
    suspend fun sendDocForSignatures(@Body document: Document): ResponseSign

    @GET("/v3/signature_request/list")
    suspend fun getSignatureRequests(): SignatureRequests

    @GET("/v3/template/list")
    suspend fun getTemplates(): Templates
}
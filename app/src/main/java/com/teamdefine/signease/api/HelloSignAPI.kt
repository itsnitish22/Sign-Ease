package com.teamdefine.signease.api

import com.teamdefine.signease.api.modelsgetrequest.SignatureRequests
import com.teamdefine.signease.api.modelspostrequest.Document
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HelloSignAPI {
    @POST("/v3/signature_request/send_with_template")
    suspend fun sendDocForSignatures(@Body document: Document)

    @GET("/v3/signature_request/list")
    suspend fun getSignatureRequests(): SignatureRequests
}
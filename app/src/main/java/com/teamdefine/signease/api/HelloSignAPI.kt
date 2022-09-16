package com.teamdefine.signease.api

import com.teamdefine.signease.api.modelsgetrequest.SignatureRequests
import retrofit2.http.GET
import retrofit2.http.POST

interface HelloSignAPI {
    @POST("/signature_request/send_with_template")
    suspend fun sendDocForSignatures()

    @GET("/v3/signature_request/list")
    suspend fun getSignatureRequests(): SignatureRequests
}
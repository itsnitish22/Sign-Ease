package com.teamdefine.signease.api

import com.teamdefine.signease.api.modelspostrequest.Document
import retrofit2.http.Body
import retrofit2.http.POST

interface HelloSignAPI {
    @POST("/signature_request/send_with_template")
    suspend fun sendDocForSignatures(@Body document: Document)
}
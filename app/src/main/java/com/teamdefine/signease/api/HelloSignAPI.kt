package com.teamdefine.signease.api

import retrofit2.http.POST

interface HelloSignAPI {
    @POST("/signature_request/send_with_template")
    suspend fun sendDocForSignatures()
}
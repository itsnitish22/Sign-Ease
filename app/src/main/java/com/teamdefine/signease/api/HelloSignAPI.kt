package com.teamdefine.signease.api

import retrofit2.http.GET
import retrofit2.http.POST

interface HelloSignAPI {
    @GET("/template/list")
    suspend fun templateList()
    @POST("/signature_request/send_with_template")
    suspend fun sendDocForSignatures()
}
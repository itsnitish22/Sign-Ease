package com.teamdefine.signease.api

import com.teamdefine.signease.api.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.signease.api.models.get_all_templates.Templates
import com.teamdefine.signease.api.models.get_download_file.Download
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.api.models.post_template_for_sign.response.ResponseSign
import retrofit2.http.*

interface HelloSignAPI {
    @Headers("Content-Type: application/json")
    @POST("/v3/signature_request/send_with_template")
    suspend fun sendDocForSignatures(@Body document: Document): ResponseSign

    @GET("/v3/signature_request/list")
    suspend fun getSignatureRequests(): SignatureRequests

    @GET("/v3/template/list")
    suspend fun getTemplates(): Templates

    @GET("v3/signature_request/files/{sign_id}")
    suspend fun getURL(
        @Path("sign_id") sign_id: String,
        @Query("get_url") get_url: Boolean
    ): Download
}
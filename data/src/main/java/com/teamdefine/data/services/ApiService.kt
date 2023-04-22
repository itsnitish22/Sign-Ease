package com.teamdefine.data.services

import com.teamdefine.data.constants.Urls
import com.teamdefine.domain.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.domain.models.get_all_templates.Templates
import com.teamdefine.domain.models.get_download_file.Download
import com.teamdefine.domain.models.post_create_app.CreateAPIApp
import com.teamdefine.domain.models.post_create_app.response.CreateAppResponse
import com.teamdefine.domain.models.post_template_for_sign.Document
import com.teamdefine.domain.models.post_template_for_sign.response.ResponseSign
import retrofit2.http.*

interface ApiService {
    @GET(Urls.GET_ALL_SIGNATURE_REQUESTS)
    suspend fun getSignatureRequests(): SignatureRequests

    @Headers("Content-Type: application/json")
    @POST(Urls.SEND_WITH_TEMPLATE_FOR_SIGNATURE)
    suspend fun sendDocForSignatures(@Body document: Document): ResponseSign

    @GET(Urls.GET_ALL_TEMPLATES_LIST)
    suspend fun getTemplates(): Templates

    @GET("${Urls.GET_FILE_URL_FOR_DOWNLOAD}/{sign_id}")
    suspend fun getURL(
        @Path("sign_id") sign_id: String,
        @Query("get_url") get_url: Boolean
    ): Download

    @POST("${Urls.DELETE_SIGNATURE_REQUEST}/{sign_id}")
    suspend fun deleteRequest(
        @Path("sign_id") sign_id: String
    )

    @Headers("Content-Type: application/json")
    @POST(Urls.HELLO_SIGN_APP)
    suspend fun createApp(@Body body: CreateAPIApp): CreateAppResponse

    @DELETE("${Urls.HELLO_SIGN_APP}/{client_id}")
    suspend fun deleteApp(@Path("client_id") clientId: String): retrofit2.Response<Unit>
}
package com.teamdefine.domain.models.get_all_sign_requests

data class SignatureRequest(
    val subject: String,
    val created_at: Long,
    val is_complete: Boolean,
    val files_url: String,
    val client_id: String,
    val signature_request_id: String
)

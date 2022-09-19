package com.teamdefine.signease.api.models.get_all_sign_requests

data class SignatureRequest(
    val subject: String,
    val created_at: Long,
    val is_complete: Boolean,
    val files_url: String
)

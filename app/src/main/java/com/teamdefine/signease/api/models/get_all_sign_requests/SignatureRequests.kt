package com.teamdefine.signease.api.models.get_all_sign_requests

data class SignatureRequests(
    val list_info: ListInfo,
    val signature_requests: ArrayList<SignatureRequest>
)

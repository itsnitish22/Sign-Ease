package com.teamdefine.signease.api.modelsgetrequest

data class SignatureRequest(
    val subject:String,
    val is_complete:Boolean,
    val files_url:String
)

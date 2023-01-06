package com.teamdefine.signease.api.models.post_create_app

data class CreateAPIApp(
    val name: String,
    val domains: ArrayList<String>
)
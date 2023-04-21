package com.teamdefine.domain.models.post_create_app

data class CreateAPIApp(
    val name: String,
    val domains: ArrayList<String>
)
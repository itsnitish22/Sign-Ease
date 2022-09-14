package com.teamdefine.signease.api.modelspostrequest

data class SigningOptions(
    val draw: Boolean = true,
    val type: Boolean = true,
    val upload: Boolean = true,
    val phone: Boolean = false,
    val default_type: String = "draw"
)
package com.teamdefine.signease.api.modelspostrequest

data class Document(
    val template_ids: ArrayList<String>,
    val subject: String,
    val message: String,
    val signers: ArrayList<Signers>,
    val custom_fields: ArrayList<CustomFields>,
    val signing_options: SigningOptions
)
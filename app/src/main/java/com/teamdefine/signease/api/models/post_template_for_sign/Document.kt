package com.teamdefine.signease.api.models.post_template_for_sign

data class Document(
    val template_ids: ArrayList<String>,
    val subject: String,
    val message: String,
    val signers: ArrayList<Signers>,
    val custom_fields: ArrayList<CustomFields>,
    val signing_options: SigningOptions,
    val test_mode: Boolean
)
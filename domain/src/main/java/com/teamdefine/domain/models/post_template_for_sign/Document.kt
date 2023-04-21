package com.teamdefine.domain.models.post_template_for_sign

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Document(
    val template_ids: ArrayList<String>,
    val subject: String,
    val message: String,
    val signers: ArrayList<Signers>,
    val custom_fields: ArrayList<CustomFields>,
    val signing_options: SigningOptions,
    val test_mode: Boolean,
    val client_id: String
) : Parcelable
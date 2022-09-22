package com.teamdefine.signease.api.models.post_template_for_sign

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomFields(
    val name: String,
    var value: String
) : Parcelable
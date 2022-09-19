package com.teamdefine.signease.api.models.post_template_for_sign

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SigningOptions(
    val draw: Boolean = true,
    val type: Boolean = true,
    val upload: Boolean = true,
    val phone: Boolean = false,
    val default_type: String = "draw"
) : Parcelable
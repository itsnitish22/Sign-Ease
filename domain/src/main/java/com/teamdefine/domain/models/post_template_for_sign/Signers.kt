package com.teamdefine.domain.models.post_template_for_sign

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Signers(
    val role: String,
    val name: String,
    val email_address: String
) : Parcelable
package com.teamdefine.signease.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

object Utility {
    fun Context.toast(message: String) {
        Toast.makeText(
            this, message,
            if (message.length <= 25) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    }

    fun Fragment.toast(msg: String) {
        requireContext().toast(msg)
    }
}
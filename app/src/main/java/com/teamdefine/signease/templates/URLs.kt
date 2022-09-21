package com.teamdefine.signease.templates

object URLs {
    val mappedUrls: MutableMap<String, String> = mutableMapOf()

    fun getUrlsFromData(): MutableMap<String, String> {
        mappedUrls["Application For Duty Leave"] =
            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/DL.pdf?alt=media&token=863e24b4-fd59-496c-ab63-7e3fb78a6476"
        return mappedUrls
    }
}
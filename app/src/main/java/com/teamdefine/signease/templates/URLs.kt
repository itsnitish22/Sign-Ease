package com.teamdefine.signease.templates

object URLs {
    val mappedUrls: MutableMap<String, String> = mutableMapOf()

    fun getUrlsFromData(): MutableMap<String, String> {
        mappedUrls["Application For Duty Leave"] =
            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/DL.pdf?alt=media&token=863e24b4-fd59-496c-ab63-7e3fb78a6476"
        mappedUrls["Day-Pass"] =
            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/Day-Pass.pdf?alt=media&token=4cc73e68-aeaf-4751-b278-5d161546cf34"
        mappedUrls["Night-Pass"] =
            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/Night-Pass.pdf?alt=media&token=7e75c17b-4812-4a33-9ebe-cae27dffc1e2"

        return mappedUrls
    }
}
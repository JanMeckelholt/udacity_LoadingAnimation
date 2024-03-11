package com.udacity.utils

import com.udacity.Constants

enum class URL(val url: String) {
    UDACITY(Constants.URL_UDACITY),
    GLIDE(Constants.URL_GLIDE),
    RETROFIT(Constants.URL_RETROFIT)
}

fun getUrl(selection : Int) : String? {
    return when(selection) {
        1-> URL.GLIDE.url
        2-> URL.UDACITY.url
        3-> URL.RETROFIT.url
        else -> null
    }
}

fun statusToStr(status : Int) : String {
    return when(status) {
        1 -> "pending"
        2 -> "running"
        4 -> "paused"
        8 -> "success"
        16 -> "failed"
        else -> "other"
    }
}
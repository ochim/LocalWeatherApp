package com.example.localweatherapp.extension

import java.util.*

fun Date.within1hourAgo(unixTime: Int?): Boolean {
    unixTime ?: return false
    val to = Date(unixTime * 1000L)
    val diff = (this.time - to.time)
    return 0 <= diff && diff <= 1000 * 60 * 60
}
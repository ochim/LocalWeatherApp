package com.example.localweatherapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
    val name: String,
    val weather: List<Weather>,
    val dt: Int?,
    val main: Temperature?
)

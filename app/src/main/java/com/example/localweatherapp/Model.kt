package com.example.localweatherapp

data class Info(
    val name: String,
    val weather: List<Weather>
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)
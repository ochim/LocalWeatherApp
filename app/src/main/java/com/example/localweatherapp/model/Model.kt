package com.example.localweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
    val name: String,
    val weather: List<Weather>,
    val dt: Int?,
    val main: Temperature?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class City(
    val name: String,
    val q: String?
)

data class Temperature(
    val temp: Double?,
    val humidity: Double?
)

@Entity(tableName = "cityweather")
data class CityWeather(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val query: String,
    var dt: Int?,
    var infoJson: String?
)
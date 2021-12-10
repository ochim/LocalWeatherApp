package com.example.localweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cityweather")
data class CityWeather(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val query: String,
    var dt: Int?,
    var infoJson: String?
)
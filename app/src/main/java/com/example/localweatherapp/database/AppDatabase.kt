package com.example.localweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localweatherapp.model.CityWeather

@Database(entities = arrayOf(CityWeather::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
}

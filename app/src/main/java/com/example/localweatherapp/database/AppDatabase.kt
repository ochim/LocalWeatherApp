package com.example.localweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localweatherapp.model.CityWeather

@Database(entities = [CityWeather::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
}

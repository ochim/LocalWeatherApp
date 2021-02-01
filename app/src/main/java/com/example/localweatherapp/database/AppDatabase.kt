package com.example.localweatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localweatherapp.model.CityWeather

@Database(entities = arrayOf(CityWeather::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
}

object Database {
    private var _appDatabase: AppDatabase? = null

    fun initialize(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database.db"
        ).build()
    }

    fun getAppDatabase() = _appDatabase!!

}
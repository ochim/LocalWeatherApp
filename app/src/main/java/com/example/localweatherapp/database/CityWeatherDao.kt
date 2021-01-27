package com.example.localweatherapp.database

import androidx.room.*
import com.example.localweatherapp.model.CityWeather

@Dao
interface CityWeatherDao {
    @Insert
    suspend fun insertAll(vararg cityWeathers: CityWeather)

    @Delete
    suspend fun delete(cityWeather: CityWeather)

    @Query("SELECT * FROM cityweather WHERE `query` == :q")
    suspend fun loadByQuery(q: String): CityWeather?

    @Update
    suspend fun update(cityWeather: CityWeather)
}

package com.example.localweatherapp.repository

import com.example.localweatherapp.BuildConfig
import com.example.localweatherapp.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val APP_ID = BuildConfig.API_KEY

interface CityWeatherInfoInterface {
    @GET("weather?lang=ja&appid=$APP_ID")
    fun getWeatherInfo(@Query("q") query: String): Call<Info>
}

class CityWeatherInfoRepository(val cityWeatherInfoInterface: CityWeatherInfoInterface) {

    suspend fun getWeatherInfo(query: String): Info? {
        return withContext(Dispatchers.IO) {
            val response = cityWeatherInfoInterface.getWeatherInfo(query).execute()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                null
            }
        }
    }

}
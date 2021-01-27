package com.example.localweatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.localweatherapp.BuildConfig
import com.example.localweatherapp.database.Database
import com.example.localweatherapp.model.CityWeather
import com.example.localweatherapp.model.Info
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

class CityWeatherInfoRepository(
    private val cityWeatherInfoInterface: CityWeatherInfoInterface,
) {

    suspend fun getWeatherInfo(query: String, errorMessage: MutableLiveData<String?>): Info? {
        return withContext(Dispatchers.IO) {
            val response = cityWeatherInfoInterface.getWeatherInfo(query).execute()
            if (response.isSuccessful) {
                val info = response.body()!!
                updateCityWeather(info, query)
                info
            } else {
                errorMessage.postValue("${response.code()} ${response.message()}")
                null
            }
        }
    }

    /*
     * アプリDBに保存した天気情報を更新する
     */
    private suspend fun updateCityWeather(info: Info, query: String) {
        val dao = Database.getAppDatabase().cityWeatherDao()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val json = moshi.adapter(Info::class.java).toJson(info)

        val cityWeather = dao.loadByQuery(query)
        if (cityWeather != null){

            cityWeather.dt = info.dt
            cityWeather.infoJson = json
            dao.update(cityWeather)
        } else {

            val city = CityWeather(0, query, info.dt, json)
            dao.insertAll(city)
        }
    }

}
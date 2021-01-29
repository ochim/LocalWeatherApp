package com.example.localweatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.localweatherapp.BuildConfig
import com.example.localweatherapp.database.Database
import com.example.localweatherapp.extension.within1hourAgo
import com.example.localweatherapp.model.CityWeather
import com.example.localweatherapp.model.Info
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

const val APP_ID = BuildConfig.API_KEY

interface CityWeatherInfoInterface {
    @GET("weather?lang=ja&appid=$APP_ID")
    fun getWeatherInfo(@Query("q") query: String): Call<Info>
}

class CityWeatherInfoRepository(
    private val cityWeatherInfoInterface: CityWeatherInfoInterface,
) {

    private val dao = Database.getAppDatabase().cityWeatherDao()
    private val adapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Info::class.java)

    suspend fun getWeatherInfo(query: String, errorMessage: MutableLiveData<String?>): Info? {
        return withContext(Dispatchers.IO) {
            val savedCityWeather = dao.loadByQuery(query)
            if (Date().within1hourAgo(savedCityWeather?.dt)) {
                //1時間前以内のデータがDBにあればそれを返す
                savedCityWeather?.infoJson?.let {
                    return@withContext adapter.fromJson(it)
                }
            }

            //WEB APIから取得する
            val response = cityWeatherInfoInterface.getWeatherInfo(query).execute()
            if (response.isSuccessful) {
                val info = response.body()!!
                updateCityWeather(info, query, savedCityWeather)
                info
            } else {
                errorMessage.postValue("${response.code()} ${response.message()}")
                //エラーなのでDBのものを返す
                savedCityWeather?.infoJson?.let {
                    return@withContext adapter.fromJson(it)
                }
            }
        }
    }

    /*
     * アプリDBに保存した天気情報を更新する
     */
    private suspend fun updateCityWeather(info: Info, query: String, savedCityWeather: CityWeather?) {
        val json = adapter.toJson(info)

        if (savedCityWeather != null){

            savedCityWeather.apply {
                dt = info.dt
                infoJson = json
                dao.update(this)
            }

        } else {

            val city = CityWeather(0, query, info.dt, json)
            dao.insertAll(city)
        }
    }

}
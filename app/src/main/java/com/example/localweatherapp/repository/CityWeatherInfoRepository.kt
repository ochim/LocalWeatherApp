package com.example.localweatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.localweatherapp.BuildConfig
import com.example.localweatherapp.database.CityWeatherDao
import com.example.localweatherapp.extension.within1hourAgo
import com.example.localweatherapp.model.CityWeather
import com.example.localweatherapp.model.Info
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.*

const val APP_ID = BuildConfig.API_KEY

interface CityWeatherInfoInterface {
    @GET("weather?lang=ja&appid=$APP_ID")
    fun getWeatherInfo(@Query("q") query: String): Call<Info>
}

class CityWeatherInfoRepository(
    private val cityWeatherInfoInterface: CityWeatherInfoInterface,
    private val dao: CityWeatherDao,
    private val infoAdapter: JsonAdapter<Info>
) {

    suspend fun getWeatherInfo(query: String, errorMessage: MutableLiveData<String?>): Info? {
        return withContext(Dispatchers.IO) {
            val savedCityWeather = dao.loadByQuery(query)
            if (Date().within1hourAgo(savedCityWeather?.dt)) {
                //1時間前以内のデータがDBにあればそれを返す
                savedCityWeather?.infoJson?.let {
                    return@withContext infoAdapter.fromJson(it)
                }
            }

            try {
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
                        return@withContext infoAdapter.fromJson(it)
                    }
                }

            } catch (ex: Exception) {
                Timber.e(ex.toString())
                errorMessage.postValue(ex.message ?: "不明なエラーが発生しました")
                //例外が起きたのでDBのものを返す
                savedCityWeather?.infoJson?.let {
                    return@withContext infoAdapter.fromJson(it)
                }
            }
        }
    }

    /*
     * アプリDBに保存した天気情報を更新する
     */
    private suspend fun updateCityWeather(info: Info, query: String, savedCityWeather: CityWeather?) {
        val json = infoAdapter.toJson(info)

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
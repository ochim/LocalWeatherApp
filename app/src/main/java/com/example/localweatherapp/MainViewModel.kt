package com.example.localweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localweatherapp.repository.CityWeatherInfoInterface
import com.example.localweatherapp.repository.CityWeatherInfoRepository
import com.example.localweatherapp.repository.WeatherInfoRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {
    private val info: MutableLiveData<Info?> = MutableLiveData()
    private val errorMessage: MutableLiveData<String?> = MutableLiveData()

    private val weatherInfoRepository = WeatherInfoRepository()
    private val cityWeatherInfoRepository = CityWeatherInfoRepository(
        weatherInfoRepository.retrofit.create(CityWeatherInfoInterface::class.java)
    )

    fun getInfo(query: String): LiveData<Info?> {
        loadInfo(query)
        return info
    }

    fun getErrorMessage(): LiveData<String?> = errorMessage

    private fun loadInfo(query: String) {
        viewModelScope.launch {
            try {
                val i = cityWeatherInfoRepository.getWeatherInfo(query, errorMessage)
                info.postValue(i)
            } catch (ex: Exception) {
                Timber.e(ex.toString())
                errorMessage.postValue(ex.message ?: "不明なエラーが発生しました")
            }
        }
    }

}
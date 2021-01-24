package com.example.localweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localweatherapp.repository.CityWeatherInfoInterface
import com.example.localweatherapp.repository.CityWeatherInfoRepository
import com.example.localweatherapp.repository.WeatherInfoRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val info: MutableLiveData<Info?> = MutableLiveData()

    private val weatherInfoRepository = WeatherInfoRepository()
    private val cityWeatherInfoRepository = CityWeatherInfoRepository(
        weatherInfoRepository.retrofit.create(CityWeatherInfoInterface::class.java)
    )

    fun getInfo(query: String): LiveData<Info?> {
        loadInfo(query)
        return info
    }

    private fun loadInfo(query: String) {
        viewModelScope.launch {
            try {
                val i = cityWeatherInfoRepository.getWeatherInfo(query)
                info.postValue(i)
            } catch (ex: Exception) {
                Log.e("error", ex.toString())
            }
        }
    }

}
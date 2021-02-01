package com.example.localweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localweatherapp.model.Info
import com.example.localweatherapp.repository.CityWeatherInfoInterface
import com.example.localweatherapp.repository.CityWeatherInfoRepository
import com.example.localweatherapp.repository.WeatherInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val info: MutableLiveData<Info?> = MutableLiveData()
    private val errorMessage: MutableLiveData<String?> = MutableLiveData()
    val progressBarStatus: MutableLiveData<Int> = MutableLiveData(android.widget.ProgressBar.INVISIBLE)

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
        progressBarStatus.postValue(android.widget.ProgressBar.VISIBLE)

        viewModelScope.launch {
            val i = cityWeatherInfoRepository.getWeatherInfo(query, errorMessage)
            info.postValue(i)
            progressBarStatus.postValue(android.widget.ProgressBar.INVISIBLE)
        }
    }

}
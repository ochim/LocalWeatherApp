package com.example.localweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localweatherapp.model.Info
import com.example.localweatherapp.repository.CityWeatherInfoRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(private val cityWeatherInfoRepository: CityWeatherInfoRepository) :
    ViewModel() {

    private val info: MutableLiveData<Info?> = MutableLiveData()
    private val errorMessage: MutableLiveData<String?> = MutableLiveData()
    val progressBarStatus: MutableLiveData<Int> =
        MutableLiveData(android.widget.ProgressBar.INVISIBLE)

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
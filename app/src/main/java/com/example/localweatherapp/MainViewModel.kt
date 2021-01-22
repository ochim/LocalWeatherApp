package com.example.localweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class MainViewModel : ViewModel() {
    private val info: MutableLiveData<Info?> = MutableLiveData()
    private val repository = MainRepository()

    fun getInfo(query: String): LiveData<Info?> {
        loadInfo(query)
        return info
    }

    private fun loadInfo(query: String) {
        val url = "$WEATHERINFO_URL&q=$query&appid=$APP_ID"
        repository.loadInfo(url, viewModelScope, info)
    }

}
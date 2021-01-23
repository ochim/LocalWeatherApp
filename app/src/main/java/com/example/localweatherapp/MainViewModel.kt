package com.example.localweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val info: MutableLiveData<Info?> = MutableLiveData()
    private val repository = MainRepository()

    fun getInfo(query: String): LiveData<Info?> {
        loadInfo(query)
        return info
    }

    private fun loadInfo(query: String) {
        val url = "$WEATHERINFO_URL&q=$query&appid=$APP_ID"
        viewModelScope.launch {
            try {
                val result = repository.backgroundTaskRunner(url)
                val i = repository.postExecutorRunner(result)
                info.postValue(i)
            } catch (ex : Exception) {
                Log.e("error", ex.toString())
            }
        }

    }

}
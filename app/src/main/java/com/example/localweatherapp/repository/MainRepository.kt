package com.example.localweatherapp.repository

import androidx.annotation.WorkerThread
import com.example.localweatherapp.model.Info
import com.example.localweatherapp.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainRepository {

    @WorkerThread
    suspend fun backgroundTaskRunner(_url: String): String {
        return withContext(Dispatchers.IO) {
            var result = ""
            val url = URL(_url)
            val con = url.openConnection() as? HttpURLConnection
            con?.run {
                requestMethod = "GET"
                connect()
                result = is2String(inputStream)
                disconnect()
                inputStream.close()
            }
            result
        }
    }

    fun postExecutorRunner(result: String): Info {
        val rootJSON = JSONObject(result)
        val cityName = rootJSON.getString("name")
        val weatherJSONArray = rootJSON.getJSONArray("weather")
        val weatherJSON = weatherJSONArray.getJSONObject(0)
        val description = weatherJSON.getString("description")
        val weather = Weather(null, null, description, null)
        return Info(cityName, listOf(weather), rootJSON.getInt("dt"))
    }

    /**
     * InputStreamオブジェクトを文字列に変換するメソッド。変換文字コードはUTF-8
     */
    private fun is2String(stream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var line = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }
}
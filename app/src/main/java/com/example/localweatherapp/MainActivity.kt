package com.example.localweatherapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * CodeZine
 * Web API連携サンプル
 * Kotlin版のスケルトンプロジェクト
 * @author Shinzo SAITO
 *
 * を元にして変更
 */
private const val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
private const val APP_ID = BuildConfig.API_KEY

class MainActivity : AppCompatActivity() {

    /**
     * リストビューに表示させるリストデータ。
     */
    private var _list: List<Map<String, String?>> =
        listOf(
            mapOf("name" to "大阪", "q" to "Osaka"),
            mapOf("name" to "神戸", "q" to "Kobe"),
            mapOf("name" to "京都", "q" to "Kyoto"),
            mapOf("name" to "大津", "q" to "Otsu"),
            mapOf("name" to "奈良", "q" to "Nara"),
            mapOf("name" to "和歌山", "q" to "Wakayama"),
            mapOf("name" to "姫路", "q" to "Himeji"),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter = SimpleAdapter(
            applicationContext, _list, android.R.layout.simple_expandable_list_item_1, from, to
        )
        lvCityList.adapter = adapter
        lvCityList.onItemClickListener = object : AdapterView.OnItemClickListener {
            /**
             * リストがタップされた時の処理が記述されたメソッド
             */
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val item = _list[position]
                val q = item["q"]
                val url = "$WEATHERINFO_URL&q=$q&appid=$APP_ID"
                asyncExecute(url)
            }
        }
    }

    /**
     * お天気情報の取得処理を行うメソッド
     * コルーチン開始
     */
    @UiThread
    fun asyncExecute(url: String) {
        lifecycleScope.launch {
            val result = backgroundTaskRunner(url)
            postExecutorRunner(result)
        }
    }

    /**
     * 非同期でお天気情報APIにアクセスするためのメソッド
     * suspendで処理を中断
     */
    @WorkerThread
    private suspend fun backgroundTaskRunner(_url: String): String {
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

    /**
     * 取得したお天気情報を画面に表示するメソッド
     */
    @UiThread
    private fun postExecutorRunner(result: String) {
        val rootJSON = JSONObject(result)
        val cityName = rootJSON.getString("name")
        val weatherJSONArray = rootJSON.getJSONArray("weather")
        val weatherJSON = weatherJSONArray.getJSONObject(0)
        val description = weatherJSON.getString("description")
        val telop = cityName + "の天気"
        val desc = "現在は" + description + "です。"

        val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
        val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)
        tvWeatherTelop.text = telop
        tvWeatherDesc.text = desc
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

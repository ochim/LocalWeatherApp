package com.example.localweatherapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

/**
 * CodeZine
 * Web API連携サンプル
 * Java版のスケルトンプロジェクト
 *
 * アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
private const val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
private const val APP_ID = BuildConfig.API_KEY

class MainActivity : AppCompatActivity() {
    /**
     * リストビューに表示させるリストデータ。
     */
    private var _list: List<Map<String, String?>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _list = createList()
        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter = SimpleAdapter(
            applicationContext, _list, android.R.layout.simple_expandable_list_item_1, from, to
        )
        lvCityList.adapter = adapter
        lvCityList.onItemClickListener = object: AdapterView.OnItemClickListener {
            /**
             * リストがタップされた時の処理が記述されたメソッド
             */
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val item = _list!![position]
                val q = item["q"]
                val url = WEATHERINFO_URL + "&q=" + q + "&appid=" + APP_ID
                asyncExecute(url)
            }
        }
    }

    private fun createList(): List<Map<String, String?>> {
        return listOf(
            mapOf("name" to "大阪", "q" to "Osaka"),
            mapOf("name" to "神戸", "q" to "Kobe"),
            mapOf("name" to "京都", "q" to "Kyoto"),
            mapOf("name" to "大津", "q" to "Otsu"),
            mapOf("name" to "奈良", "q" to "Nara"),
        )
    }

    companion object {
        private const val DEBUG_TAG = "AsyncTest"
    }

    /**
     * お天気情報の取得処理を行うメソッド。
     */
    @UiThread
    fun asyncExecute(url: String) {
        val mainLooper = Looper.getMainLooper()
        val handler = HandlerCompat.createAsync(mainLooper)
        val backgroundTask = BackgroundTask(handler, url)
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit(backgroundTask)
    }

    /**
     * 非同期でお天気情報APIにアクセスするためのクラス
     * @param _handler UIスレッドを表すハンドラオブジェクト
     * @param _url リクエストURL
     */
    private inner class BackgroundTask(
        private var _handler: Handler,
        private var _url: String? = null
    ) : Runnable {

        @WorkerThread
        override fun run() {
            val url = URL(_url)
            var con: HttpURLConnection? = null
            var inputStream: InputStream? = null
            var result = ""

            try {
                con = url.openConnection() as HttpURLConnection
                con.setRequestMethod("GET")
                con.connect()
                inputStream = con.getInputStream()
                result = is2String(inputStream)
            } catch (ex: MalformedURLException) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex)
            } catch (ex: IOException) {
                Log.e(DEBUG_TAG, "通信失敗", ex)
            } finally {
                if (con != null) {
                    con.disconnect()
                }
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (ex: IOException) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex)
                    }
                }
            }

            val postExecutor = PostExecutor(result)
            _handler.post(postExecutor)
        }

        /**
         * InputStreamオブジェクトを文字列に変換するメソッド。 変換文字コードはUTF-8。
         *
         * @param inputStream 変換対象のInputStreamオブジェクト。
         * @return 変換された文字列。
         * @throws IOException 変換に失敗した時に発生。
         */
        @Throws(IOException::class)
        private fun is2String(inputStream: InputStream): String {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val sb = StringBuffer()
            val b = CharArray(1024)
            var line: Int
            while (0 <= reader.read(b).also { line = it }) {
                sb.append(b, 0, line)
            }
            return sb.toString()
        }
    }

    /**
     * 非同期でお天気情報を取得した後にUIスレッドでその情報を表示するためのクラス。
     */
    private inner class PostExecutor(private val _result: String) : Runnable {
        @UiThread
        override fun run() {
            var cityName = ""
            var weather = ""
            try {
                val rootJSON = JSONObject(_result)
                cityName = rootJSON.getString("name")
                val weatherJSONArray: JSONArray = rootJSON.getJSONArray("weather")
                val weatherJSON: JSONObject = weatherJSONArray.getJSONObject(0)
                weather = weatherJSON.getString("description")
            } catch (ex: JSONException) {
                Log.e(DEBUG_TAG, "JSON解析失敗", ex)
            }

            val telop = cityName + "の天気"
            val desc = "現在は" + weather + "です。"

            val tvWeatherTelop: TextView = findViewById(R.id.tvWeatherTelop)
            val tvWeatherDesc: TextView = findViewById(R.id.tvWeatherDesc)

            tvWeatherTelop.setText(telop)
            tvWeatherDesc.setText(desc)
        }
    }

}

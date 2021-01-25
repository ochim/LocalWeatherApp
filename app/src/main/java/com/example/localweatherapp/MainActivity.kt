package com.example.localweatherapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

/**
 * CodeZine
 * Web API連携サンプル
 * Kotlin版のスケルトンプロジェクト
 * @author Shinzo SAITO
 *
 * を元にして変更
 */

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

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val model: MainViewModel by viewModels()

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
                model.getInfo(q!!).observe(this@MainActivity, {
                    it?.let { updateUI(it) }
                })
            }
        }

    }

    @UiThread
    fun updateUI(info: Info) {
        val telop = info.name + "の天気"
        val weather = info.weather[0]
        val description = weather.description ?: ""
        val desc = "現在は" + description + "です。"

        val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
        val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)
        tvWeatherTelop.text = telop
        tvWeatherDesc.text = desc

        weather.icon ?: return
        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(iconUrl(weather.icon)).centerCrop()
            .error(R.drawable.no_image).into(imageView)
    }

    private fun iconUrl(name: String) = "https://openweathermap.org/img/wn/$name@2x.png"

}

package com.example.localweatherapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.localweatherapp.databinding.ActivityMainBinding
import com.example.localweatherapp.model.Info
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

/**
 * CodeZine
 * Web API連携サンプル
 * Kotlin版のスケルトンプロジェクト
 * @author Shinzo SAITO
 *
 * を元にして変更
 */
@AndroidEntryPoint
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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val model: MainViewModel by viewModels()

        val lvCityList = binding.lvCityList
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
                val q = _list[position]["q"]
                model.getInfo(q!!).observe(this@MainActivity, {
                    it?.let { updateUI(it) }
                })
            }
        }

        model.getErrorMessage().observe(this, {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        model.progressBarStatus.observe(this, {
            binding.progressBar.visibility = it
        })

    }

    @UiThread
    fun updateUI(info: Info) {
        val telop = info.name + "の天気"
        val weather = info.weather[0]
        val description = weather.description ?: ""
        val desc = "現在は" + description + "です。"

        binding.tvWeatherTelop.text = telop
        binding.tvWeatherDesc.text = desc

        weather.icon?.let {
            Glide.with(this).load(iconUrl(it)).centerCrop()
                .error(R.drawable.no_image).into(binding.imageView)
        }

        info.dt?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
            binding.tvWeatherTime.text = getString(R.string.tv_wtime_text, sdf.format(Date(it * 1000L)))
        }
    }

    private fun iconUrl(name: String) = "https://openweathermap.org/img/wn/$name@2x.png"

}

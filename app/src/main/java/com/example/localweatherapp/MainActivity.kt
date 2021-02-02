package com.example.localweatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.example.localweatherapp.adapter.CityAdapter
import com.example.localweatherapp.databinding.ActivityMainBinding
import com.example.localweatherapp.model.City
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
     * 地点として表示させるリストデータ。
     */
    private var _list: List<City> = listOf(
            City("大阪", "Osaka"),
            City("神戸", "Kobe"),
            City("京都", "Kyoto"),
            City("大津", "Otsu"),
            City("奈良", "Nara"),
            City("和歌山", "Wakayama"),
            City("姫路", "Himeji"),
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val model: MainViewModel by viewModels()

        val rvCityList = binding.recyclerView
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvCityList.addItemDecoration(itemDecoration)

        val adapter = CityAdapter(applicationContext, _list) { city ->
            /**
             * リストがタップされた時の処理
             */
            model.getInfo(city.q!!).observe(this@MainActivity, {
                it?.let { updateUI(it) }
            })
        }
        rvCityList.adapter = adapter

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
    private fun updateUI(info: Info) {
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

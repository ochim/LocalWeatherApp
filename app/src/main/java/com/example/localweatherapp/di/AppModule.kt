package com.example.localweatherapp.di

import com.example.localweatherapp.model.City
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    /**
     * 地点として表示させる都市データ。
     */
    @Provides
    fun provideCityList(): List<City> =
        listOf(
            City("札幌", "Sapporo"),
            City("仙台", "Sendai"),
            City("東京", "Tokyo"),
            City("横浜", "Yokohama"),
            City("名古屋", "Nagoya"),
            City("大阪", "Osaka"),
            City("神戸", "Kobe"),
            City("京都", "Kyoto"),
            City("大津", "Otsu"),
            City("奈良", "Nara"),
            City("和歌山", "Wakayama"),
            City("姫路", "Himeji"),
            City("広島", "Hiroshima"),
            City("福岡", "Fukuoka"),
            City("鹿児島", "Kagoshima"),
            City("那覇", "Naha"),
        )
}
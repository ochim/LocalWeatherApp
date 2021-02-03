package com.example.localweatherapp.di

import com.example.localweatherapp.database.CityWeatherDao
import com.example.localweatherapp.model.Info
import com.example.localweatherapp.repository.CityWeatherInfoInterface
import com.example.localweatherapp.repository.CityWeatherInfoRepository
import com.example.localweatherapp.repository.WeatherInfoRepository
import com.squareup.moshi.JsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeathInfoRepository() : WeatherInfoRepository = WeatherInfoRepository()

    @Provides
    @Singleton
    fun provideCityWeatherInfoRepository(weatherInfoRepository: WeatherInfoRepository,
                                         dao: CityWeatherDao,
                                         infoAdapter: JsonAdapter<Info>
    ): CityWeatherInfoRepository {
        return CityWeatherInfoRepository(
            weatherInfoRepository.retrofit.create(CityWeatherInfoInterface::class.java), dao , infoAdapter)
    }
}


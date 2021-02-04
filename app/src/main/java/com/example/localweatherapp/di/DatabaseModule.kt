package com.example.localweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.localweatherapp.database.AppDatabase
import com.example.localweatherapp.database.CityWeatherDao
import com.example.localweatherapp.model.Info
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database.db"
    ).build()

    @Provides
    fun provideDao(appDatabase: AppDatabase): CityWeatherDao = appDatabase.cityWeatherDao()

    @Provides
    fun provideInfoAdapter(): JsonAdapter<Info> = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(
        Info::class.java)
}

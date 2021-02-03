package com.example.localweatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Tree

@HiltAndroidApp
class MainApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

    }

    companion object {
        private var instance: MainApplication? = null

        fun getInstance(): MainApplication = instance!!
    }

    /** A tree which logs important information for crash reporting.  */
    inner class CrashReportingTree : Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        }
    }
}
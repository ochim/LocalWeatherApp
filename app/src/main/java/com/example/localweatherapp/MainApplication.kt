package com.example.localweatherapp

import android.app.Application
import com.example.localweatherapp.database.Database
import timber.log.Timber
import timber.log.Timber.Tree


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

        Database.initialize(applicationContext)
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
package com.example.localweatherapp

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Tree


class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting.  */
    inner class CrashReportingTree : Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        }
    }
}
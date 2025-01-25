package com.xvantage.rental.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.d("BaseApplication", "Application started")
    }
}

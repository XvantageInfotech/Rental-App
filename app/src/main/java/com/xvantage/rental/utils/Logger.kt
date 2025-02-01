package com.xvantage.rental.utils

import android.util.Log
import com.xvantage.rental.BuildConfig

object Logger {
    fun e(key: String?, value: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(key, value!!)
        }
    }

    fun d(key: String?, value: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(key, value!!)
        }
    }

    fun i(key: String?, value: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(key, value!!)
        }
    }
}

package com.xvantage.rental.utils

import android.util.Log
import com.xvantage.rental.BuildConfig


object Debugger {
    const val TAG: String = "LOG"

    private val LOG_DISPLAY: Boolean = BuildConfig.IS_DEBUG

    fun logE(message: String?) {
        if (LOG_DISPLAY) Log.e(TAG, message!!)
    }

    fun logE(tag: String, message: String) {
        if (LOG_DISPLAY) Log.e(TAG, "$tag --> $message")
    }

    fun logD(message: String?) {
        if (LOG_DISPLAY) Log.d(TAG, message!!)
    }

    fun logD(tag: String, message: String) {
        if (LOG_DISPLAY) Log.d(TAG, "$tag --> $message")
    }

    fun logI(message: String?) {
        if (LOG_DISPLAY) Log.i(TAG, message!!)
    }

    fun logI(tag: String, message: String) {
        if (LOG_DISPLAY) Log.i(TAG, "$tag --> $message")
    }
}

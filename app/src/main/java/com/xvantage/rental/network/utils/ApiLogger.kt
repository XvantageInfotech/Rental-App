package com.xvantage.rental.network.utils

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  07/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

object ApiLogger {

    private const val TAG = "API_LOGGER"

    fun logRequest(endpoint: String, requestBody: Any?) {
        Log.d(TAG, "📤 API Request: $endpoint")
        Log.d(TAG, "📨 Request Body: ${Gson().toJson(requestBody)}")
    }

    fun <T> logResponse(endpoint: String, response: Response<T>) {
        Log.d(TAG, "✅ API Response: $endpoint")
        Log.d(TAG, "🔢 Status Code: ${response.code()}")
        Log.d(TAG, "📄 Response Body: ${Gson().toJson(response.body())}")

        if (!response.isSuccessful) {
            Log.e(TAG, "❌ API Error: ${response.message()}")
            Log.e(TAG, "⚠️ Error Body: ${response.errorBody()?.string()}")
        }
    }

    fun logError(endpoint: String, exception: Exception) {
        Log.e(TAG, "🚨 Network Error: $endpoint")
        Log.e(TAG, "⚠️ Exception: ${exception.localizedMessage}")
    }
}

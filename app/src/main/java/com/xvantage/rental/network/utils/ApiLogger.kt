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
import okhttp3.Request
import retrofit2.Response

object ApiLogger {

    private const val TAG = "API_LOGGER"

    fun logRequest(requestBody: Any?, request: Request) {
        val endpoint = request.url.toString()
        Log.d(TAG, buildBlockLog("📤 API Request", endpoint, Gson().toJson(requestBody)))
    }

    fun <T> logResponse(response: Response<T>, request: Request) {
        val endpoint = request.url.toString()
        val responseBody = Gson().toJson(response.body())

        Log.d(TAG, buildBlockLog("✅ API Response", endpoint, responseBody, response.code()))

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, buildBlockLog("❌ API Error", endpoint, errorBody, response.code()))
        }
    }

    fun logError(exception: Exception, request: Request?) {
        val endpoint = request?.url.toString()
        Log.e(TAG, buildBlockLog("🚨 Network Error", endpoint, exception.localizedMessage))
    }

    private fun buildBlockLog(title: String, endpoint: String, body: String?, code: Int? = null): String {
        val line = "──────────────────────────────────"
        return """
        |$line
        |$title
        |$line
        |📍 URL: $endpoint
        |${if (code != null) "🔢 Status Code: $code" else ""}
        |📄 Data: ${body ?: "No Data"}
        |$line
        """.trimMargin()
    }
}

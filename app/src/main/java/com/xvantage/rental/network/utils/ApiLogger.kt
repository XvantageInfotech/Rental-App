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
import okhttp3.MultipartBody
import okhttp3.Request
import retrofit2.Response

object ApiLogger {

    private const val TAG = "API_LOGGER"

    fun logRequest(requestBody: Any?, request: Request?) {
        val endpoint = request?.url?.toString() ?: "Unknown Endpoint"

        // Convert the request body to a loggable format
        val formattedBody = when(requestBody) {
            is Map<*, *> -> Gson().toJson(requestBody)
            is MultipartBody -> formatMultipartBody(requestBody)
            is HashMap<*, *> -> Gson().toJson(requestBody)
            else -> Gson().toJson(requestBody)
        }

        Log.d(TAG, buildBlockLog("📤 API Request", endpoint, formattedBody))
    }

    fun <T> logResponse(response: Response<T>, request: Request?) {
        val endpoint = request?.url?.toString() ?: "Unknown Endpoint"
        val responseBody = Gson().toJson(response.body())

        Log.d(TAG, buildBlockLog("✅ API Response", endpoint, responseBody, response.code()))

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, buildBlockLog("❌ API Error", endpoint, errorBody, response.code()))
        }
    }

    fun logError(exception: Exception, request: Request?) {
        val endpoint = request?.url?.toString() ?: "Unknown Endpoint"
        Log.e(TAG, buildBlockLog("🚨 Network Error", endpoint, exception.localizedMessage))
    }

    private fun buildBlockLog(title: String, endpoint: String, body: String?, code: Int? = null): String {
        val line = "──────────────────────────────────────────────────────────────────────────────────"
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

    /**
     * Format a MultipartBody for logging
     */
    private fun formatMultipartBody(multipartBody: MultipartBody): String {
        val parts = StringBuilder()
        parts.append("MultipartBody {\n")

        multipartBody.parts.forEach { part ->
            val headers = part.headers
            if (headers != null) {
                val contentDisposition = headers["Content-Disposition"]
                if (contentDisposition != null) {
                    val nameMatch = "name=\"([^\"]+)\"".toRegex().find(contentDisposition)
                    val filenameMatch = "filename=\"([^\"]+)\"".toRegex().find(contentDisposition)

                    val name = nameMatch?.groupValues?.getOrNull(1) ?: "unknown"
                    val hasFile = filenameMatch != null

                    if (hasFile) {
                        val filename = filenameMatch?.groupValues?.getOrNull(1) ?: "unknown"
                        parts.append("  $name: File($filename)\n")
                    } else {
                        parts.append("  $name: [Form data]\n")
                    }
                }
            }
        }

        parts.append("}")
        return parts.toString()
    }
}
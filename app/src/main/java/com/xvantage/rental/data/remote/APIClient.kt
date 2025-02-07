package com.xvantage.rental.data.remote

import com.google.gson.GsonBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.utils.Utility
import com.xvantage.rental.utils.constants.ApiConstant
import com.xvantage.rental.utils.libs.loggingInterceptor.Level
import com.xvantage.rental.utils.libs.loggingInterceptor.LoggingInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {


    fun appInterfaceServerUser(): APIInterface {
        return getClient(BuildConfig.SERVER_USER).create(APIInterface::class.java)
    }


    private fun getClient(baseUrl: String): Retrofit {
        val httpClient = OkHttpClient.Builder()

        // Adding custom headers
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader(ApiConstant.HEADER_CONTENT_TYPE, ApiConstant.CONTENT_FORM_DATA)
            }.build()
            chain.proceed(request)
        }

        // Adding LoggingInterceptor
        httpClient.addInterceptor(
            LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build()
        )

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}

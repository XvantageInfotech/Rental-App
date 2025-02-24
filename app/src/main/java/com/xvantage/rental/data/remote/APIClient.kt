package com.xvantage.rental.data.remote

import com.google.gson.GsonBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.utils.constants.ApiConstant
import com.xvantage.rental.utils.libs.loggingInterceptor.Level
import com.xvantage.rental.utils.libs.loggingInterceptor.LoggingInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    fun appInterfaceServerUser(): APIInterface {
        return getClient("https://api.rental.xvantageinfotech.com/api/v1/").create(APIInterface::class.java)
    }

    private fun getClient(baseUrl: String): Retrofit {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader(ApiConstant.HEADER_CONTENT_TYPE, ApiConstant.CONTENT_TYPE_JSON)
                addHeader("roletype", "landlord")
                addHeader("requesttoken", "610904831af1a01c5251e5437c53421338a01032a0c01bcc7db9da73368e339b")
            }.build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(
            LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build()
        )

        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
    }

    fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}

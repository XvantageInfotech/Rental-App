package com.xvantage.rental.data.remote

import com.google.gson.JsonObject
import com.xvantage.rental.network.request.auth.LoginRequest
import com.xvantage.rental.network.request.auth.GoogleLoginRequest
import com.xvantage.rental.network.request.auth.SignupRequest
import com.xvantage.rental.network.request.auth.VerifyOTPRequest
import com.xvantage.rental.network.response.LoginResponse
import com.xvantage.rental.network.response.SignupResponse
import com.xvantage.rental.network.response.VerifyOTPResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIInterface {

    @GET
    fun get(@Url url: String): Call<JsonObject>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap params: Map<String, String>): Call<JsonObject>

    @POST("landlord/auth/login")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<LoginResponse>

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignupRequest): Response<SignupResponse>

    @POST("auth/verify")
    suspend fun verifyOtp(@Body request: VerifyOTPRequest): Response<VerifyOTPResponse>
    @POST("landlord/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

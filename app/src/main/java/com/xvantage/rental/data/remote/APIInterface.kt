package com.xvantage.rental.data.remote

import com.google.gson.JsonObject
import com.xvantage.rental.network.request.LoginRequest
import com.xvantage.rental.network.request.auth.GoogleLoginRequest
import com.xvantage.rental.network.request.auth.SignupRequest
import com.xvantage.rental.network.request.auth.VerifyOTPRequest
import com.xvantage.rental.network.response.LoginResponse
import com.xvantage.rental.network.response.SignupResponse
import com.xvantage.rental.network.response.VerifyOTPResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url

interface APIInterface {

    @GET
    fun get(@Url url: String): Call<JsonObject>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap params: Map<String, String>): Call<JsonObject>


    @POST("auth/google-login")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignupRequest): Response<SignupResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOTPRequest): Response<VerifyOTPResponse>
    @Multipart
    @POST
    fun reachedDrop(
        @Url url: String,
        @Part("userID") userId: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<JsonObject>

    @Multipart
    @POST
    fun addTollParking(
        @Url url: String,
        @Part file: MultipartBody.Part
    ): Call<JsonObject>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST
    fun addDutySlip(
        @Url url: String,
        @Part pdf: MultipartBody.Part
    ): Call<JsonObject>

    @Multipart
    @POST
    fun updateProfile(
        @Url url: String,
        @PartMap params: Map<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<JsonObject>
}

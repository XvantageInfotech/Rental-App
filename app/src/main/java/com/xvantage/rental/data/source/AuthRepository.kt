package com.xvantage.rental.data.source

import com.google.gson.JsonObject
import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.network.NetworkHelper
import com.xvantage.rental.network.ResultWrapper
import com.xvantage.rental.network.request.LoginRequest
import com.xvantage.rental.network.response.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiInterface: APIInterface) {

    suspend fun login(email: String, password: String): ResultWrapper<LoginResponse> {
        val request = LoginRequest(email, password)
        return try {
            val response = apiInterface.login(request)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    /*suspend fun signUp(userData: UserData): ResultWrapper<JsonObject> {
        return try {
            val response = apiInterface.signUp(userData).execute()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body() ?: JsonObject())
            } else {
                ResultWrapper.Error(
                    message = "Signup failed: ${response.message()}",
                    statusCode = response.code(),
                    errorBody = response.errorBody()?.string()
                )
            }
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }*/

    suspend fun verifyOtp(otp: String): ResultWrapper<JsonObject> {
        val params = mapOf("otp" to otp)
        return try {
            val response = apiInterface.post("verifyOtp", params).execute()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body() ?: JsonObject())
            } else {
                ResultWrapper.Error(
                    message = "OTP verification failed: ${response.message()}",
                    statusCode = response.code(),
                    errorBody = response.errorBody()?.string()
                )
            }
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    suspend fun forgotPassword(email: String): ResultWrapper<JsonObject> {
        val params = mapOf("email" to email)
        return try {
            val response = apiInterface.post("forgotPassword", params).execute()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body() ?: JsonObject())
            } else {
                ResultWrapper.Error(
                    message = "Password reset failed: ${response.message()}",
                    statusCode = response.code(),
                    errorBody = response.errorBody()?.string()
                )
            }
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }
}

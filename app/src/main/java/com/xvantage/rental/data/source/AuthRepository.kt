package com.xvantage.rental.data.source

import com.google.gson.JsonObject
import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.network.utils.NetworkHelper
import com.xvantage.rental.network.utils.ResultWrapper
import com.xvantage.rental.network.request.LoginRequest
import com.xvantage.rental.network.request.auth.GoogleLoginRequest
import com.xvantage.rental.network.request.auth.SignupRequest
import com.xvantage.rental.network.request.auth.VerifyOTPRequest
import com.xvantage.rental.network.response.LoginResponse
import com.xvantage.rental.network.response.SignupResponse
import com.xvantage.rental.network.response.VerifyOTPResponse
import com.xvantage.rental.network.utils.ApiLogger
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiInterface: APIInterface) {

    // Login API
    suspend fun login(email: String, password: String): ResultWrapper<LoginResponse> {
        val request = LoginRequest(email, password)

        return try {
            val response = apiInterface.login(request)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    // Google Login API
    suspend fun loginWithGoogle(googleData: GoogleLoginRequest): ResultWrapper<LoginResponse> {
        return try {
            val response = apiInterface.googleLogin(googleData)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    // Signup API
    suspend fun signUp(email: String, password: String): ResultWrapper<SignupResponse> {
        val request = SignupRequest(email, password)
        return try {
            val response = apiInterface.signUp(request)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    // Verify OTP API
    suspend fun verifyOtp(email: String, otp: String, type: String): ResultWrapper<VerifyOTPResponse> {
        val request = VerifyOTPRequest(email, otp, type)
        return try {
            val response = apiInterface.verifyOtp(request)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }

    // Forgot Password API
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

package com.xvantage.rental.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.AuthRepository
import com.xvantage.rental.network.request.auth.GoogleLoginRequest
import com.xvantage.rental.network.utils.ResultWrapper
import com.xvantage.rental.ui.auth.fragment.sealed.AuthScreen
import com.xvantage.rental.ui.auth.fragment.sealed.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val authStateFlow = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = authStateFlow.asStateFlow()

    private val currentScreenFlow = MutableStateFlow<AuthScreen>(AuthScreen.SignIn)
    val currentScreen: StateFlow<AuthScreen> = currentScreenFlow.asStateFlow()

    fun setCurrentScreen(screen: AuthScreen) {
        currentScreenFlow.value = screen
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authStateFlow.value = AuthState.Loading
            when (val response = repository.login(email, password)) {
                is ResultWrapper.Success -> {
                    authStateFlow.value = AuthState.Success(response.value.data?.token)
                    currentScreenFlow.value = AuthScreen.VerifyOtp
                }
                is ResultWrapper.Error -> {
                    authStateFlow.value = AuthState.Error(response.message ?: "Login failed")
                }
                ResultWrapper.Loading -> TODO()
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authStateFlow.value = AuthState.Loading
            when (val response = repository.signUp(email, password)) {
                is ResultWrapper.Success -> {
                    authStateFlow.value = AuthState.Success("Signup successful")
                    currentScreenFlow.value = AuthScreen.VerifyOtp
                }
                is ResultWrapper.Error -> {
                    authStateFlow.value = AuthState.Error(response.message ?: "Signup failed")
                }
                ResultWrapper.Loading -> TODO()
            }
        }
    }

    fun verifyOtp(email: String, otp: String, type: String) {
        viewModelScope.launch {
            authStateFlow.value = AuthState.Loading
            when (val response = repository.verifyOtp(email, otp, type)) {
                is ResultWrapper.Success -> {
                    authStateFlow.value = AuthState.Success("OTP Verified")
                    currentScreenFlow.value = AuthScreen.Dashboard
                }
                is ResultWrapper.Error -> {
                    authStateFlow.value = AuthState.Error(response.message ?: "Invalid OTP")
                }
                ResultWrapper.Loading -> TODO()
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            authStateFlow.value = AuthState.Loading
            when (val response = repository.forgotPassword(email)) {
                is ResultWrapper.Success -> {
                    authStateFlow.value = AuthState.Success("Password reset link sent")
                }
                is ResultWrapper.Error -> {
                    authStateFlow.value = AuthState.Error(response.message ?: "Invalid email")
                }
                ResultWrapper.Loading -> TODO()
            }
        }
    }

    fun loginWithGoogle(googleData: GoogleLoginRequest) {
        viewModelScope.launch {
            authStateFlow.value = AuthState.Loading
            when (val response = repository.loginWithGoogle(googleData)) {
                is ResultWrapper.Success -> {
                    authStateFlow.value = AuthState.Success(response.value.data?.token)
                    currentScreenFlow.value = AuthScreen.VerifyOtp
                }
                is ResultWrapper.Error -> {
                    authStateFlow.value = AuthState.Error(response.message ?: "Google Login failed")
                }
                ResultWrapper.Loading -> TODO()
            }
        }
    }
}

package com.xvantage.rental.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  03/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
//    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentScreen = MutableStateFlow<AuthScreen>(AuthScreen.SignIn)
    val currentScreen: StateFlow<AuthScreen> = _currentScreen.asStateFlow()

    fun setCurrentScreen(screen: AuthScreen) {
        _currentScreen.value = screen
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
//                val response = repository.login(email, password)
                _currentScreen.value = AuthScreen.VerifyOtp // Move to OTP screen after login

                /*                if (response.isSuccessful) {
                                    _authState.value = AuthState.Success(response.body()?.token)
                                    _currentScreen.value = AuthScreen.VerifyOtp // Move to OTP screen after login
                                } else {
                                    _authState.value = AuthState.Error("Invalid credentials")
                                }*/
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp(/*userData: UserData*/) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _currentScreen.value = AuthScreen.VerifyOtp
/*
            val result = repository.signUp(*/
/*userData*//*
)
*/
            /*_authState.value = if (result) AuthState.Success("User registered") else AuthState.Error("Signup failed")
            if (result) _currentScreen.value = AuthScreen.VerifyOtp*/
        }
    }

    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _currentScreen.value = AuthScreen.Dashboard
           /* val result = repository.verifyOtp(otp)
            _authState.value = if (result) AuthState.Success("OTP Verified") else AuthState.Error("Invalid OTP")
            if (result) _currentScreen.value = AuthScreen.Dashboard*/
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _currentScreen.value = AuthScreen.Dashboard
//            _authState.value = AuthState.Loading
//            val result = repository.forgotPassword(email)
//            _authState.value = if (result) AuthState.Success("Reset link sent") else AuthState.Error("Invalid email")
        }
    }
}



package com.xvantage.rental.ui.auth

data class SignInUIState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val userEmail: String? = null,
    val authToken: String? = null,
    val errorMessage: String? = null
)
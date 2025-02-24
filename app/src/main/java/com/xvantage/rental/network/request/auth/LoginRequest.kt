package com.xvantage.rental.network.request.auth

data class LoginRequest(
    val email: String,
    val password: String,
    val type: String
)

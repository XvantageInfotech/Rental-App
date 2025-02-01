package com.xvantage.rental.network.response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val email: String,
    val token: String,

)
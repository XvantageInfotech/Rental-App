package com.xvantage.rental.network.response

data class PropertyDetailsResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data?,
    val err: Any?
) {
    data class Data(
        val name: String,
        val email: String,
        val login_type: String,
        val is_profile_complete: Boolean,
        val token: String
    )
}
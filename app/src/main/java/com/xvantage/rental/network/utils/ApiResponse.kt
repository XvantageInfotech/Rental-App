package com.xvantage.rental.network.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T> Type of the data.
 */
sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: Exception, val message: String? = null) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}
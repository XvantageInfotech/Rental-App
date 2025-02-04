package com.xvantage.rental.network.utils


import retrofit2.Response

object NetworkHelper {

    fun <T> handleApiResponse(response: Response<T>): ResultWrapper<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                ResultWrapper.Success(it)
            } ?: ResultWrapper.Error("Empty response body", response.code())
        } else {
            val errorBody = response.errorBody()?.string()
            ResultWrapper.Error(
                message = "Error: ${response.code()} - ${response.message()}",
                statusCode = response.code(),
                errorBody = errorBody
            )
        }
    }
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(
        val message: String,
        val statusCode: Int? = null, // HTTP status code
        val errorBody: String? = null // Optional raw error body
    ) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}

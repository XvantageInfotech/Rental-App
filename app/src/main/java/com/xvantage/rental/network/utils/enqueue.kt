package com.xvantage.rental.network.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> Call<T>.enqueue(
    onSuccess: (response: T) -> Unit,
    onError: (error: String) -> Unit
) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let(onSuccess) ?: onError("Response body is null")
            } else {
                onError("Error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(t.message ?: "Unknown error")
        }
    })
}
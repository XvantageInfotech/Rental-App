package com.xvantage.rental.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ResponseModel(
    @SerializedName("status") @Expose val status: Int,
    @SerializedName("message") @Expose val message: String
)

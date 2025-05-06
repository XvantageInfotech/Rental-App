package com.xvantage.rental.data.source

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.network.request.property.CreatePropertyRequest
import com.xvantage.rental.network.response.CreatePropertyResponse
import com.xvantage.rental.network.response.PropertyType
import com.xvantage.rental.network.utils.ApiLogger
import com.xvantage.rental.network.utils.NetworkHelper
import com.xvantage.rental.network.utils.ResultWrapper
import jakarta.inject.Inject

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  06/05/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */


class PropertyRepository @Inject constructor(private val apiInterface: APIInterface) {

    suspend fun createProperty(request: CreatePropertyRequest): ResultWrapper<CreatePropertyResponse> {
        val retrofitRequest = apiInterface.createProperty(request).raw().request
        return try {
            ApiLogger.logRequest(request, retrofitRequest)
            val response = apiInterface.createProperty(request)
            ApiLogger.logResponse(response, retrofitRequest)
            NetworkHelper.handleApiResponse(response)
        } catch (e: Exception) {
            ApiLogger.logError(e, retrofitRequest)
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }
    }
    suspend fun getPropertyTypes(): ResultWrapper<List<PropertyType>> {
        return try {
            val response = apiInterface.get("room-type/property-type")
            val wrapper = NetworkHelper.handleApiResponse(response)
            when (wrapper) {
                is ResultWrapper.Success -> {
                    val jsonObject = wrapper.value
                    val dataArray = jsonObject.getAsJsonArray("data")
                    val listType = object : TypeToken<List<PropertyType>>() {}.type
                    val list: List<PropertyType> = Gson().fromJson(dataArray, listType)
                    ResultWrapper.Success(list)
                }

                is ResultWrapper.Error -> wrapper
                else -> ResultWrapper.Error("Unexpected response type")
            }
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.localizedMessage}")
        }

    }
}
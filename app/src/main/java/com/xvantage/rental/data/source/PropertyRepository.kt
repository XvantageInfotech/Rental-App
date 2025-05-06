package com.xvantage.rental.data.source

import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.network.request.property.CreatePropertyRequest
import com.xvantage.rental.network.response.CreatePropertyResponse
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
}
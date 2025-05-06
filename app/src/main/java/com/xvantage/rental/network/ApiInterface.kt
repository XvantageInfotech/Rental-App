package com.xvantage.rental.network

import com.xvantage.rental.data.model.Property
import com.xvantage.rental.data.model.PropertyType
import com.xvantage.rental.data.model.Room
import com.xvantage.rental.data.model.Tenant
import com.xvantage.rental.network.utils.ApiResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Interface for defining API endpoints
 */
interface ApiService {

    @GET("propertyTypes")
    suspend fun getPropertyTypes(): ApiResponse<List<PropertyType>>

    @Multipart
    @POST("properties")
    suspend fun createProperty(
        @Part("address") address: RequestBody,
        @Part("noOfRoom") noOfRoom: RequestBody,
        @Part("propertyTypeId") propertyTypeId: RequestBody,
        @Part("wa_number") waNumber: RequestBody,
        @Part("name") name: RequestBody,
        @Part propertyImage: MultipartBody.Part?
    ): ApiResponse<Property>

    @GET("properties/{id}")
    suspend fun getPropertyDetails(@Path("id") propertyId: String): ApiResponse<Property>

    @GET("properties/{id}/rooms")
    suspend fun getPropertyRooms(@Path("id") propertyId: String): ApiResponse<List<Room>>

    @Multipart
    @POST("rooms")
    suspend fun addRoom(
        @Part("propertyId") propertyId: RequestBody,
        @Part("roomNo") roomNo: RequestBody,
        @Part("roomTypeText") roomTypeText: RequestBody,
        @Part("address") address: RequestBody,
        @Part roomImage: MultipartBody.Part?,
        @Part("roomTypeId") roomTypeId: RequestBody,
        @Part("rent") rent: RequestBody,
        @Part("meterReading") meterReading: RequestBody,
        @Part("meterReadingLastDate") meterReadingLastDate: RequestBody,
        @Part("propertyTypeId") propertyTypeIdBody: RequestBody // Renamed to avoid conflict
    ): ApiResponse<Room>

    @Multipart
    @PUT("rooms/{id}")
    suspend fun updateRoom(
        @Path("id") roomId: String,
        @Part("propertyId") propertyId: RequestBody,
        @Part("roomNo") roomNo: RequestBody,
        @Part("roomTypeText") roomTypeText: RequestBody,
        @Part("address") address: RequestBody,
        @Part roomImage: MultipartBody.Part?,
        @Part("roomTypeId") roomTypeId: RequestBody,
        @Part("rent") rent: RequestBody,
        @Part("meterReading") meterReading: RequestBody,
        @Part("meterReadingLastDate") meterReadingLastDate: RequestBody,
        @Part("propertyTypeId") propertyTypeIdBody: RequestBody // Renamed to avoid conflict
    ): ApiResponse<Room>

    @GET("properties/{id}/tenants")
    suspend fun getPropertyTenants(@Path("id") propertyId: String): ApiResponse<List<Tenant>>

    @Multipart
    @POST("tenants")
    suspend fun addTenant(
        @Part profilePic: MultipartBody.Part?,
        @Part("roomId") roomId: RequestBody,
        @Part("tenantName") tenantName: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("phoneCode") phoneCode: RequestBody,
        @Part("roomDeposit") roomDeposit: RequestBody,
        @Part("checkinDate") checkinDate: RequestBody,
        @Part("rentStartDate") rentStartDate: RequestBody,
        @Part("rentSubmissionDate") rentSubmissionDate: RequestBody
    ): ApiResponse<Tenant>

    @Multipart
    @PUT("tenants/{id}")
    suspend fun updateTenant(
        @Path("id") tenantId: String,
        @Part profilePic: MultipartBody.Part?,
        @Part("roomId") roomId: RequestBody,
        @Part("tenantName") tenantName: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("phoneCode") phoneCode: RequestBody,
        @Part("roomDeposit") roomDeposit: RequestBody,
        @Part("checkinDate") checkinDate: RequestBody,
        @Part("rentStartDate") rentStartDate: RequestBody,
        @Part("rentSubmissionDate") rentSubmissionDate: RequestBody
    ): ApiResponse<Tenant>
}
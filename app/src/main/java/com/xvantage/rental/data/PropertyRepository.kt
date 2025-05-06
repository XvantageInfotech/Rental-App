package com.xvantage.rental.data

import com.xvantage.rental.data.model.Property
import com.xvantage.rental.data.model.PropertyType
import com.xvantage.rental.data.model.Room
import com.xvantage.rental.data.model.Tenant
import com.xvantage.rental.network.ApiService
import com.xvantage.rental.network.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for handling property related data operations.
 * It abstracts the data sources (remote/local) from the ViewModels.
 */
@Singleton
class PropertyRepository @Inject constructor(
    private val apiService: ApiService
) {

    private fun String.toRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun File.toMultipartBodyPart(partName: String): MultipartBody.Part {
        val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, this.name, requestFile)
    }

    fun getPropertyTypes(): Flow<ApiResponse<List<PropertyType>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.getPropertyTypes()
            emit(response) // Assuming ApiService directly returns ApiResponse
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun createProperty(
        address: String,
        noOfRoom: String,
        propertyTypeId: String,
        waNumber: String,
        name: String,
        propertyImageFile: File?
    ): Flow<ApiResponse<Property>> = flow {
        emit(ApiResponse.Loading)
        try {
            val imagePart = propertyImageFile?.toMultipartBodyPart("propertyImage")
            val response = apiService.createProperty(
                address = address.toRequestBody(),
                noOfRoom = noOfRoom.toRequestBody(),
                propertyTypeId = propertyTypeId.toRequestBody(),
                waNumber = waNumber.toRequestBody(),
                name = name.toRequestBody(),
                propertyImage = imagePart
            )
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to create property"))
        }
    }.flowOn(Dispatchers.IO)

    fun getPropertyDetails(propertyId: String): Flow<ApiResponse<Property>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.getPropertyDetails(propertyId)
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to fetch property details"))
        }
    }.flowOn(Dispatchers.IO)

    fun getPropertyRooms(propertyId: String): Flow<ApiResponse<List<Room>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.getPropertyRooms(propertyId)
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to fetch rooms"))
        }
    }.flowOn(Dispatchers.IO)

    fun addRoom(
        propertyId: String,
        roomNo: String,
        roomTypeText: String,
        address: String,
        roomImageFile: File?,
        roomTypeId: String,
        rent: String,
        meterReading: String,
        meterReadingLastDate: String,
        propertyTypeIdBody: String // Renamed to avoid conflict in ApiService
    ): Flow<ApiResponse<Room>> = flow {
        emit(ApiResponse.Loading)
        try {
            val imagePart = roomImageFile?.toMultipartBodyPart("roomImage")
            val response = apiService.addRoom(
                propertyId = propertyId.toRequestBody(),
                roomNo = roomNo.toRequestBody(),
                roomTypeText = roomTypeText.toRequestBody(),
                address = address.toRequestBody(),
                roomImage = imagePart,
                roomTypeId = roomTypeId.toRequestBody(),
                rent = rent.toRequestBody(),
                meterReading = meterReading.toRequestBody(),
                meterReadingLastDate = meterReadingLastDate.toRequestBody(),
                propertyTypeIdBody = propertyTypeIdBody.toRequestBody()
            )
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to add room"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateRoom(
        roomId: String,
        propertyId: String,
        roomNo: String,
        roomTypeText: String,
        address: String,
        roomImageFile: File?,
        roomTypeId: String,
        rent: String,
        meterReading: String,
        meterReadingLastDate: String,
        propertyTypeIdBody: String // Renamed to avoid conflict in ApiService
    ): Flow<ApiResponse<Room>> = flow {
        emit(ApiResponse.Loading)
        try {
            val imagePart = roomImageFile?.toMultipartBodyPart("roomImage")
            val response = apiService.updateRoom(
                roomId = roomId,
                propertyId = propertyId.toRequestBody(),
                roomNo = roomNo.toRequestBody(),
                roomTypeText = roomTypeText.toRequestBody(),
                address = address.toRequestBody(),
                roomImage = imagePart,
                roomTypeId = roomTypeId.toRequestBody(),
                rent = rent.toRequestBody(),
                meterReading = meterReading.toRequestBody(),
                meterReadingLastDate = meterReadingLastDate.toRequestBody(),
                propertyTypeIdBody = propertyTypeIdBody.toRequestBody()
            )
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to update room"))
        }
    }.flowOn(Dispatchers.IO)

    fun getPropertyTenants(propertyId: String): Flow<ApiResponse<List<Tenant>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.getPropertyTenants(propertyId)
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to fetch tenants"))
        }
    }.flowOn(Dispatchers.IO)

    fun addTenant(
        profilePicFile: File?,
        roomId: String,
        tenantName: String,
        phoneNumber: String,
        phoneCode: String,
        roomDeposit: String,
        checkinDate: String,
        rentStartDate: String,
        rentSubmissionDate: String
    ): Flow<ApiResponse<Tenant>> = flow {
        emit(ApiResponse.Loading)
        try {
            val imagePart = profilePicFile?.toMultipartBodyPart("profilePic")
            val response = apiService.addTenant(
                profilePic = imagePart,
                roomId = roomId.toRequestBody(),
                tenantName = tenantName.toRequestBody(),
                phoneNumber = phoneNumber.toRequestBody(),
                phoneCode = phoneCode.toRequestBody(),
                roomDeposit = roomDeposit.toRequestBody(),
                checkinDate = checkinDate.toRequestBody(),
                rentStartDate = rentStartDate.toRequestBody(),
                rentSubmissionDate = rentSubmissionDate.toRequestBody()
            )
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to add tenant"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateTenant(
        tenantId: String,
        profilePicFile: File?,
        roomId: String,
        tenantName: String,
        phoneNumber: String,
        phoneCode: String,
        roomDeposit: String,
        checkinDate: String,
        rentStartDate: String,
        rentSubmissionDate: String
    ): Flow<ApiResponse<Tenant>> = flow {
        emit(ApiResponse.Loading)
        try {
            val imagePart = profilePicFile?.toMultipartBodyPart("profilePic")
            val response = apiService.updateTenant(
                tenantId = tenantId,
                profilePic = imagePart,
                roomId = roomId.toRequestBody(),
                tenantName = tenantName.toRequestBody(),
                phoneNumber = phoneNumber.toRequestBody(),
                phoneCode = phoneCode.toRequestBody(),
                roomDeposit = roomDeposit.toRequestBody(),
                checkinDate = checkinDate.toRequestBody(),
                rentStartDate = rentStartDate.toRequestBody(),
                rentSubmissionDate = rentSubmissionDate.toRequestBody()
            )
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e, e.localizedMessage ?: "Failed to update tenant"))
        }
    }.flowOn(Dispatchers.IO)
}
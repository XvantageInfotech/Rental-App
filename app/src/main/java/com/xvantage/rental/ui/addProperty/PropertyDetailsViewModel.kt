package com.xvantage.rental.ui.addProperty

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.PropertyRepository
import com.xvantage.rental.data.model.Property
import com.xvantage.rental.data.model.Room
import com.xvantage.rental.data.model.Tenant
import com.xvantage.rental.network.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

// --- UI States ---
sealed class PropertyDetailUiState {
    object Loading : PropertyDetailUiState()
    data class Success(val property: Property) : PropertyDetailUiState()
    data class Error(val message: String) : PropertyDetailUiState()
    object Idle : PropertyDetailUiState()
}

sealed class RoomsUiState {
    object Loading : RoomsUiState()
    data class Success(val rooms: List<Room>) : RoomsUiState()
    data class Error(val message: String) : RoomsUiState()
    object Idle : RoomsUiState()
}

sealed class TenantsUiState {
    object Loading : TenantsUiState()
    data class Success(val tenants: List<Tenant>) : TenantsUiState()
    data class Error(val message: String) : TenantsUiState()
    object Idle : TenantsUiState()
}

sealed class RoomMutationUiState {
    object Idle : RoomMutationUiState()
    object Loading : RoomMutationUiState()
    data class Success(val room: Room, val isUpdate: Boolean) : RoomMutationUiState()
    data class Error(val message: String) : RoomMutationUiState()
}

sealed class TenantMutationUiState {
    object Idle : TenantMutationUiState()
    object Loading : TenantMutationUiState()
    data class Success(val tenant: Tenant, val isUpdate: Boolean) : TenantMutationUiState()
    data class Error(val message: String) : TenantMutationUiState()
}

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val repository: PropertyRepository,
    private val savedStateHandle: SavedStateHandle // For potentially passing propertyId via navigation args
) : ViewModel() {

    private val _propertyDetailState = MutableStateFlow<PropertyDetailUiState>(PropertyDetailUiState.Idle)
    val propertyDetailState: StateFlow<PropertyDetailUiState> = _propertyDetailState.asStateFlow()

    private val _roomsState = MutableStateFlow<RoomsUiState>(RoomsUiState.Idle)
    val roomsState: StateFlow<RoomsUiState> = _roomsState.asStateFlow()

    private val _tenantsState = MutableStateFlow<TenantsUiState>(TenantsUiState.Idle)
    val tenantsState: StateFlow<TenantsUiState> = _tenantsState.asStateFlow()

    private val _roomMutationState = MutableStateFlow<RoomMutationUiState>(RoomMutationUiState.Idle)
    val roomMutationState: StateFlow<RoomMutationUiState> = _roomMutationState.asStateFlow()

    private val _tenantMutationState = MutableStateFlow<TenantMutationUiState>(TenantMutationUiState.Idle)
    val tenantMutationState: StateFlow<TenantMutationUiState> = _tenantMutationState.asStateFlow()

    private var currentPropertyId: String? = null

    // Call this when PropertyDetailsActivity starts or receives a new propertyId
    fun loadInitialData(propertyId: String) {
        if (propertyId.isEmpty()) {
            _propertyDetailState.value = PropertyDetailUiState.Error("Property ID is missing.")
            return
        }
        // Avoid reloading if already loaded and id is same, unless forced
        if (currentPropertyId == propertyId && _propertyDetailState.value is PropertyDetailUiState.Success) {
             // Optionally, refresh rooms and tenants if needed, or rely on specific refresh actions
            fetchRooms(propertyId)
            fetchTenants(propertyId)
            return
        }
        currentPropertyId = propertyId
        fetchPropertyDetails(propertyId)
        fetchRooms(propertyId)
        fetchTenants(propertyId)
    }

    private fun fetchPropertyDetails(propertyId: String) {
        viewModelScope.launch {
            _propertyDetailState.value = PropertyDetailUiState.Loading
            repository.getPropertyDetails(propertyId)
                .catch { e -> _propertyDetailState.value = PropertyDetailUiState.Error(e.localizedMessage ?: "Failed to load property details") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> _propertyDetailState.value = PropertyDetailUiState.Success(response.data)
                        is ApiResponse.Error -> _propertyDetailState.value = PropertyDetailUiState.Error(response.message ?: "Unknown error fetching property details")
                        is ApiResponse.Loading -> { /* Already handled by initial emit */ }
                    }
                }
        }
    }

    fun fetchRooms(propertyId: String? = currentPropertyId) {
        propertyId ?: return
        viewModelScope.launch {
            _roomsState.value = RoomsUiState.Loading
            repository.getPropertyRooms(propertyId)
                .catch { e -> _roomsState.value = RoomsUiState.Error(e.localizedMessage ?: "Failed to load rooms") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> _roomsState.value = RoomsUiState.Success(response.data)
                        is ApiResponse.Error -> _roomsState.value = RoomsUiState.Error(response.message ?: "Unknown error fetching rooms")
                        is ApiResponse.Loading -> { /* Already handled */ }
                    }
                }
        }
    }

    fun fetchTenants(propertyId: String? = currentPropertyId) {
        propertyId ?: return
        viewModelScope.launch {
            _tenantsState.value = TenantsUiState.Loading
            repository.getPropertyTenants(propertyId)
                .catch { e -> _tenantsState.value = TenantsUiState.Error(e.localizedMessage ?: "Failed to load tenants") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> _tenantsState.value = TenantsUiState.Success(response.data)
                        is ApiResponse.Error -> _tenantsState.value = TenantsUiState.Error(response.message ?: "Unknown error fetching tenants")
                        is ApiResponse.Loading -> { /* Already handled */ }
                    }
                }
        }
    }

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
        propertyTypeIdBody: String // Matches ApiService param name
    ) {
        viewModelScope.launch {
            _roomMutationState.value = RoomMutationUiState.Loading
            repository.addRoom(propertyId, roomNo, roomTypeText, address, roomImageFile, roomTypeId, rent, meterReading, meterReadingLastDate, propertyTypeIdBody)
                .catch { e -> _roomMutationState.value = RoomMutationUiState.Error(e.localizedMessage ?: "Failed to add room") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _roomMutationState.value = RoomMutationUiState.Success(response.data, false)
                            fetchRooms() // Refresh room list
                        }
                        is ApiResponse.Error -> _roomMutationState.value = RoomMutationUiState.Error(response.message ?: "Unknown error adding room")
                        is ApiResponse.Loading -> { /* Handled */ }
                    }
                }
        }
    }

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
        propertyTypeIdBody: String // Matches ApiService param name
    ) {
        viewModelScope.launch {
            _roomMutationState.value = RoomMutationUiState.Loading
            repository.updateRoom(roomId, propertyId, roomNo, roomTypeText, address, roomImageFile, roomTypeId, rent, meterReading, meterReadingLastDate, propertyTypeIdBody)
                .catch { e -> _roomMutationState.value = RoomMutationUiState.Error(e.localizedMessage ?: "Failed to update room") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _roomMutationState.value = RoomMutationUiState.Success(response.data, true)
                            fetchRooms() // Refresh room list
                        }
                        is ApiResponse.Error -> _roomMutationState.value = RoomMutationUiState.Error(response.message ?: "Unknown error updating room")
                        is ApiResponse.Loading -> { /* Handled */ }
                    }
                }
        }
    }

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
    ) {
        viewModelScope.launch {
            _tenantMutationState.value = TenantMutationUiState.Loading
            repository.addTenant(profilePicFile, roomId, tenantName, phoneNumber, phoneCode, roomDeposit, checkinDate, rentStartDate, rentSubmissionDate)
                .catch { e -> _tenantMutationState.value = TenantMutationUiState.Error(e.localizedMessage ?: "Failed to add tenant") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _tenantMutationState.value = TenantMutationUiState.Success(response.data, false)
                            fetchTenants() // Refresh tenant list
                        }
                        is ApiResponse.Error -> _tenantMutationState.value = TenantMutationUiState.Error(response.message ?: "Unknown error adding tenant")
                        is ApiResponse.Loading -> { /* Handled */ }
                    }
                }
        }
    }

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
    ) {
        viewModelScope.launch {
            _tenantMutationState.value = TenantMutationUiState.Loading
            repository.updateTenant(tenantId, profilePicFile, roomId, tenantName, phoneNumber, phoneCode, roomDeposit, checkinDate, rentStartDate, rentSubmissionDate)
                .catch { e -> _tenantMutationState.value = TenantMutationUiState.Error(e.localizedMessage ?: "Failed to update tenant") }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _tenantMutationState.value = TenantMutationUiState.Success(response.data, true)
                            fetchTenants() // Refresh tenant list
                        }
                        is ApiResponse.Error -> _tenantMutationState.value = TenantMutationUiState.Error(response.message ?: "Unknown error updating tenant")
                        is ApiResponse.Loading -> { /* Handled */ }
                    }
                }
        }
    }
    
    fun resetRoomMutationState(){
        _roomMutationState.value = RoomMutationUiState.Idle
    }

    fun resetTenantMutationState(){
        _tenantMutationState.value = TenantMutationUiState.Idle
    }
}
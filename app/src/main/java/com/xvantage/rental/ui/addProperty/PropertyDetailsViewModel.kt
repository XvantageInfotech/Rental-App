package com.xvantage.rental.ui.addProperty

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  24/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */


class PropertyDetailsViewModel(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    private val _propertyState = MutableLiveData<PropertyDetailsState>()
    val propertyState: LiveData<PropertyDetailsState> = _propertyState

    private val _roomsState = MutableLiveData<RoomsState>()
    val roomsState: LiveData<RoomsState> = _roomsState

    private val _tenantsState = MutableLiveData<TenantsState>()
    val tenantsState: LiveData<TenantsState> = _tenantsState

    private val _financialsState = MutableLiveData<FinancialsState>()
    val financialsState: LiveData<FinancialsState> = _financialsState

    fun loadPropertyDetails(propertyId: String) {
        _propertyState.value = PropertyDetailsState.Loading

        viewModelScope.launch {
            try {
                val property = propertyRepository.getPropertyById(propertyId)
                _propertyState.value = PropertyDetailsState.Success(property)
            } catch (e: Exception) {
                _propertyState.value = PropertyDetailsState.Error(e.message ?: "Failed to load property details")
            }
        }
    }

    fun loadRooms(propertyId: String) {
        _roomsState.value = RoomsState.Loading

        viewModelScope.launch {
            try {
                val rooms = propertyRepository.getRoomsByPropertyId(propertyId)
                _roomsState.value = RoomsState.Success(rooms)
            } catch (e: Exception) {
                _roomsState.value = RoomsState.Error(e.message ?: "Failed to load rooms")
            }
        }
    }

    fun loadTenants(propertyId: String) {
        _tenantsState.value = TenantsState.Loading

        viewModelScope.launch {
            try {
                val tenants = propertyRepository.getTenantsByPropertyId(propertyId)
                _tenantsState.value = TenantsState.Success(tenants)
            } catch (e: Exception) {
                _tenantsState.value = TenantsState.Error(e.message ?: "Failed to load tenants")
            }
        }
    }

    fun loadFinancials(propertyId: String) {
        _financialsState.value = FinancialsState.Loading

        viewModelScope.launch {
            try {
                val financials = propertyRepository.getPropertyFinancials(propertyId)
                _financialsState.value = FinancialsState.Success(financials)
            } catch (e: Exception) {
                _financialsState.value = FinancialsState.Error(e.message ?: "Failed to load financials")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRoom(room: Room) {
        viewModelScope.launch {
            try {
                propertyRepository.addRoom(room)
                loadRooms(room.propertyId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTenant(tenant: Tenant) {
        viewModelScope.launch {
            try {
                propertyRepository.addTenant(tenant)
                loadTenants(tenant.propertyId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

// State definition for Property Details Screen
sealed class PropertyDetailsState {
    object Loading : PropertyDetailsState()
    data class Success(val property: Property) : PropertyDetailsState()
    data class Error(val message: String) : PropertyDetailsState()
}

// State for individual tabs
sealed class RoomsState {
    object Loading : RoomsState()
    data class Success(val rooms: List<Room>) : RoomsState()
    data class Error(val message: String) : RoomsState()
}

sealed class TenantsState {
    object Loading : TenantsState()
    data class Success(val tenants: List<Tenant>) : TenantsState()
    data class Error(val message: String) : TenantsState()
}

sealed class FinancialsState {
    object Loading : FinancialsState()
    data class Success(val financials: PropertyFinancials) : FinancialsState()
    data class Error(val message: String) : FinancialsState()
}
@Parcelize
data class PropertyFinancials(
    val propertyId: String,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val monthlyRevenue: Map<String, Double> = emptyMap(),
    val lastUpdated: String = ""
) : Parcelable
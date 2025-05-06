package com.xvantage.rental.ui.addProperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.PropertyRepository
import com.xvantage.rental.data.model.Property
import com.xvantage.rental.data.model.PropertyType
import com.xvantage.rental.network.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

sealed class AddPropertyUiState {
    object Idle : AddPropertyUiState()
    object Loading : AddPropertyUiState()
    data class Success(val property: Property) : AddPropertyUiState()
    data class Error(val message: String) : AddPropertyUiState()
}

sealed class PropertyTypesUiState {
    object Loading : PropertyTypesUiState()
    data class Success(val propertyTypes: List<PropertyType>) : PropertyTypesUiState()
    data class Error(val message: String) : PropertyTypesUiState()
    object Idle : PropertyTypesUiState()
}

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    // StateFlow for property types list
    private val _propertyTypesState = MutableStateFlow<PropertyTypesUiState>(PropertyTypesUiState.Idle)
    val propertyTypesState: StateFlow<PropertyTypesUiState> = _propertyTypesState.asStateFlow()

    // StateFlow for create property operation result
    private val _createPropertyState = MutableStateFlow<AddPropertyUiState>(AddPropertyUiState.Idle)
    val createPropertyState: StateFlow<AddPropertyUiState> = _createPropertyState.asStateFlow()

    // Form state - you can expand this with MutableStateFlow for each field if needed for validation
    // For simplicity, we'll pass them directly to the createProperty function for now.

    init {
        fetchPropertyTypes()
    }

    fun fetchPropertyTypes() {
        viewModelScope.launch {
            _propertyTypesState.value = PropertyTypesUiState.Loading
            repository.getPropertyTypes()
                .catch { e ->
                    _propertyTypesState.value = PropertyTypesUiState.Error(e.localizedMessage ?: "Failed to load property types")
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> _propertyTypesState.value = PropertyTypesUiState.Success(response.data)
                        is ApiResponse.Error -> _propertyTypesState.value = PropertyTypesUiState.Error(response.message ?: "Unknown error fetching property types")
                        is ApiResponse.Loading -> _propertyTypesState.value = PropertyTypesUiState.Loading // Already handled by initial emit
                    }
                }
        }
    }

    fun createProperty(
        address: String,
        noOfRoom: String,
        propertyTypeId: String,
        waNumber: String,
        name: String,
        propertyImageFile: File?
    ) {
        viewModelScope.launch {
            _createPropertyState.value = AddPropertyUiState.Loading
            repository.createProperty(address, noOfRoom, propertyTypeId, waNumber, name, propertyImageFile)
                .catch { e ->
                    _createPropertyState.value = AddPropertyUiState.Error(e.localizedMessage ?: "Failed to create property")
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> _createPropertyState.value = AddPropertyUiState.Success(response.data)
                        is ApiResponse.Error -> _createPropertyState.value = AddPropertyUiState.Error(response.message ?: "Unknown error creating property")
                        is ApiResponse.Loading -> _createPropertyState.value = AddPropertyUiState.Loading // Already handled
                    }
                }
        }
    }
    
    fun resetCreatePropertyState(){
        _createPropertyState.value = AddPropertyUiState.Idle
    }
}
package com.xvantage.rental.ui.addProperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.PropertyRepository
import com.xvantage.rental.network.request.property.CreatePropertyRequest
import com.xvantage.rental.network.response.CreatePropertyResponse
import com.xvantage.rental.network.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  06/05/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */



sealed class CreatePropertyState {
    object Idle : CreatePropertyState()
    object Loading : CreatePropertyState()
    data class Success(val data: CreatePropertyResponse) : CreatePropertyState()
    data class Error(val message: String) : CreatePropertyState()
}

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    private val _createPropertyState = MutableStateFlow<CreatePropertyState>(CreatePropertyState.Idle)
    val createPropertyState: StateFlow<CreatePropertyState> = _createPropertyState.asStateFlow()

    fun createProperty(request: CreatePropertyRequest) {
        viewModelScope.launch {
            _createPropertyState.value = CreatePropertyState.Loading
            when (val response = repository.createProperty(request)) {
                is ResultWrapper.Success -> {
                    _createPropertyState.value = CreatePropertyState.Success(response.value)
                }
                is ResultWrapper.Error -> {
                    _createPropertyState.value = CreatePropertyState.Error(response.message ?: "Create property failed")
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }
}
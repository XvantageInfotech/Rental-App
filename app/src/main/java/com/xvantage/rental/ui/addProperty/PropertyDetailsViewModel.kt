package com.xvantage.rental.ui.addProperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.PropertyRepository
import com.xvantage.rental.network.response.PropertyDetailsResponse
import com.xvantage.rental.network.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val details: PropertyDetailsResponse) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    fun loadPropertyDetails(propertyId: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val res = repository.getPropertyDetails(propertyId)) {
                is ResultWrapper.Success -> _state.value = State.Success(res.value)
                is ResultWrapper.Error   -> _state.value = State.Error(res.message)
                else -> Unit
            }
        }
    }
}

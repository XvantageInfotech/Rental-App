package com.xvantage.rental.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.AuthRepository
import com.xvantage.rental.network.ResultWrapper
import com.xvantage.rental.network.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<ResultWrapper<Any>>(ResultWrapper.Loading)
    val loginState: StateFlow<ResultWrapper<Any>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ResultWrapper.Loading
            val result = authRepository.login(email, password)
            _loginState.value = result
        }
    }
}

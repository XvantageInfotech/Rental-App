package com.xvantage.rental.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xvantage.rental.data.source.AuthRepository
import com.xvantage.rental.network.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {


    private val loginStateMutable = MutableStateFlow(SignInUIState())
    val loginState: StateFlow<SignInUIState> = loginStateMutable

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginStateMutable.value = loginStateMutable.value.copy(isLoading = true)
            val result = authRepository.login(email, password)
            when (result) {
                is ResultWrapper.Success -> {
                    val response = result.value
                    loginStateMutable.value = loginStateMutable.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userEmail = response.email,
                        authToken = response.token
                    )
                }
                is ResultWrapper.Error -> {
                    loginStateMutable.value = loginStateMutable.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is ResultWrapper.Loading -> {
                    loginStateMutable.value = loginStateMutable.value.copy(isLoading = true)
                }
            }
        }
    }
}

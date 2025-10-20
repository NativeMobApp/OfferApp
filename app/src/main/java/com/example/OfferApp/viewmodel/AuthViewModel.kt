package com.example.OfferApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.registerUser(email, password)
            _state.value = if (result.isSuccess) AuthState.Success else AuthState.Error(result.exceptionOrNull()?.message)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.loginUser(email, password)
            _state.value = if (result.isSuccess) AuthState.Success else AuthState.Error(result.exceptionOrNull()?.message)
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String?) : AuthState()
    }
}

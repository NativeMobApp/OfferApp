package com.example.OfferApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userEmail: String?) : AuthState()
    data class PasswordResetSuccess(val message: String) : AuthState() // Cambiado para ser más específico
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.registerUser(email, password)
            _state.value = result.fold(
                onSuccess = { AuthState.Success(it?.email) },
                onFailure = { AuthState.Error(it.message ?: "Error al registrar") }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.loginUser(email, password)
            _state.value = result.fold(
                onSuccess = { AuthState.Success(it?.email) },
                onFailure = { AuthState.Error(it.message ?: "Error al iniciar sesión") }
            )
        }
    }
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.resetPassword(email)
            _state.value = result.fold(
                onSuccess = { AuthState.PasswordResetSuccess("Se envió un mail a $email") }, // Usando el nuevo estado
                onFailure = { AuthState.Error(it.message ?: "Error al enviar mail") }
            )
        }
    }
    fun logout() = repository.logout()
}
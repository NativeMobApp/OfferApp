package com.example.OfferApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    // Success state now holds both uid and email
    data class Success(val uid: String, val email: String) : AuthState()
    data class PasswordResetSuccess(val message: String) : AuthState()
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
                onSuccess = { user ->
                    if (user?.uid != null && user.email != null) {
                        AuthState.Success(user.uid, user.email!!)
                    } else {
                        AuthState.Error("Error de registro: no se pudo obtener la información del usuario.")
                    }
                },
                onFailure = { AuthState.Error(it.message ?: "Error al registrar") }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.loginUser(email, password)
            _state.value = result.fold(
                onSuccess = { user ->
                    if (user?.uid != null && user.email != null) {
                        AuthState.Success(user.uid, user.email!!)
                    } else {
                        AuthState.Error("Error de inicio de sesión: no se pudo obtener la información del usuario.")
                    }
                },
                onFailure = { AuthState.Error(it.message ?: "Error al iniciar sesión") }
            )
        }
    }
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = repository.resetPassword(email)
            _state.value = result.fold(
                onSuccess = { AuthState.PasswordResetSuccess("Se envió un mail a $email") },
                onFailure = { AuthState.Error(it.message ?: "Error al enviar mail") }
            )
        }
    }
    fun logout() {
        repository.logout()
        _state.value = AuthState.Idle
    }
}
package com.example.OfferApp.view.logIn


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    var state = LoginState()
        private set

    fun onEmailChange(newEmail: String) {
        state = state.copy(email = newEmail)
    }

    fun onPasswordChange(newPass: String) {
        state = state.copy(password = newPass)
    }

    fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = repository.login(state.email, state.password)
            state = if (result.isSuccess) {
                state.copy(isLoading = false, success = true)
            } else {
                state.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
}

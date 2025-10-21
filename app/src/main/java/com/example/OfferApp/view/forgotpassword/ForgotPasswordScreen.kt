package com.example.OfferApp.view.forgotpassword

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.OfferApp.viewmodel.AuthState
import com.example.OfferApp.viewmodel.AuthViewModel


@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onPasswordReset: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.resetPassword(email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar mail de recuperación")
        }

        when (state) {
            is AuthState.Loading -> CircularProgressIndicator()

            is AuthState.PasswordReset -> {
                Text((state as AuthState.PasswordReset).message)
                onPasswordReset() // vuelve al login
            }

            is AuthState.Error -> Text("Error: ${(state as AuthState.Error).message}")

            else -> {}
        }

    }
}

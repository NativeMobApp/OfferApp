package com.example.OfferApp.view.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.OfferApp.viewmodel.AuthViewModel
import com.example.OfferApp.viewmodel.AuthState

@Composable
fun LogInScreen(
    viewModel: AuthViewModel,
    onSuccess: (uid: String, email: String) -> Unit, // Now passes both uid and email
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
            Text("Crear cuenta")
        }

        TextButton(onClick = onForgotClick, modifier = Modifier.fillMaxWidth()) {
            Text("Olvidé mi contraseña")
        }

        Spacer(Modifier.height(8.dp))

        when (val S = state) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Success -> onSuccess(S.uid, S.email) // Pass both uid and email
            is AuthState.Error -> Text("Error: ${S.message}")
            else -> {}
        }
    }
}

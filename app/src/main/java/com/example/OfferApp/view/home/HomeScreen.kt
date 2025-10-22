package com.example.OfferApp.view.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp



@Composable
fun HomeScreen(
    header: @Composable () -> Unit
) {

    // Usamos Scaffold para proporcionar la estructura básica de la pantalla (AppBar, FloatingActionButton, etc.)
    Scaffold(
        topBar = { header() } // Coloca el header personalizado en el slot topBar
        // Si usas un BottomBar, lo colocarías aquí.
    ) { paddingValues ->
        // El resto del contenido de tu Home Screen (LazyColumn de productos, banners, etc.)
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "¡Bienvenido a OfferApp!", style = MaterialTheme.typography.headlineMedium)
            // ... aquí iría el resto del contenido de la Home
        }
    }
}
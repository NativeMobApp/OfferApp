package com.example.OfferApp.view.header

import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.OfferApp.R


@Composable
fun Header(
    onSearchClicked: () -> Unit,
    onSesionClicked: () -> Unit,
    onLogoClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = Color(0xFFD32F2F),
        shadowElevation = 4.dp
    ) {

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            TopRow(onSesionClicked, onLogoClicked, onSearchClicked)
        }
    }
}

// ----------------------------------------------------------------------

@Composable
fun TopRow(
    onSesionClicked: () -> Unit,
    onLogoClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .clickable(onClick = onLogoClicked)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_percent_discount_24),
                contentDescription = "Logo de OfferApp",
                modifier = Modifier.size(60.dp)
            )

        }
        SearchBar(
            onSearchClicked = onSearchClicked,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onSesionClicked) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Perfil / Cerrar Sesión",
                tint = Color.White
            )
        }

    }
}

// ----------------------------------------------------------------------

@Composable
fun SearchBar(onSearchClicked: () -> Unit,modifier: Modifier) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onSearchClicked)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Buscar productos...", color = Color.Gray)
        }
    }
}
package com.example.OfferApp.view.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.OfferApp.R
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    query: String,
    onQueryChange: (String) -> Unit,
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
            TopRow(query, onQueryChange,onSesionClicked, onLogoClicked)
        }
    }
}

// ----------------------------------------------------------------------

@Composable
fun TopRow(
    query: String,
    onQueryChange: (String) -> Unit,
    onSesionClicked: () -> Unit,
    onLogoClicked: () -> Unit,
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
            query = query,
            onQueryChange = onQueryChange,
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
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder ={ Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxHeight()) {
                Text("Buscar productos...", color = Color.Gray)
            }
                         },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = Color.Gray) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().height(48.dp),


            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(lineHeight = 18.sp, color = Color.Black)

        )
    }
}
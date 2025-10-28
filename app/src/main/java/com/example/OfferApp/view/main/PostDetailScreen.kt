package com.example.OfferApp.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.view.header.Header
import com.example.OfferApp.viewmodel.MainViewModel

@Composable
fun PostDetailScreen(
    mainViewModel: MainViewModel,
    post: Post,
    onBackClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            Header(
                query = mainViewModel.searchQuery, // Read query from ViewModel
                onQueryChange = { mainViewModel.onSearchQueryChange(it) }, // Update query in ViewModel
                onBackClicked = onBackClicked,
                onSesionClicked = onLogoutClicked
            )
        }
    ) { paddingValues ->
        PostDetailContent(
            mainViewModel = mainViewModel,
            post = post,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun PostDetailContent(
    mainViewModel: MainViewModel,
    post: Post,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = post.imageUrl.replace("http://", "https://"),
            contentDescription = "Post image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = post.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ubicación: ${post.location}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Latitud: ${post.latitude}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Longitud: ${post.longitude}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Publicado por: ${post.user?.email ?: "Usuario desconocido"}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Scoring buttons ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { mainViewModel.updatePostScore(post.id, 1) }) {
                Icon(Icons.Default.ThumbUp, contentDescription = "Like")
            }
            Text(
                text = "${post.scores.sumOf { it.value }}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { mainViewModel.updatePostScore(post.id, -1) }) {
                Icon(Icons.Default.ThumbDown, contentDescription = "Dislike")
            }
        }
    }
}
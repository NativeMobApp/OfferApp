package com.example.OfferApp.view.main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.view.header.Header
import com.example.OfferApp.viewmodel.MainViewModel
import androidx.compose.material.icons.filled.LocationOn

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigateToCreatePost: () -> Unit,
    onPostClick: (String) -> Unit, // Now expects the post ID (String)
    onLogoutClicked: () -> Unit,
    onNavigateToMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        topBar = {
            Header(
                query = mainViewModel.searchQuery, // Read query from ViewModel
                onQueryChange = { mainViewModel.onSearchQueryChange(it) }, // Update query in ViewModel
                onSesionClicked = onLogoutClicked,
                onLogoClicked = { /* TODO: acción clic logo */ }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre los botones
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = onNavigateToMap,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Show map")
                }
                FloatingActionButton(onClick = onNavigateToCreatePost) {
                    Icon(Icons.Default.Add, contentDescription = "Add post")
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (isLandscape) {
            Row(modifier = Modifier.padding(paddingValues)) {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(mainViewModel.posts) { post ->
                            PostItem(mainViewModel = mainViewModel, post = post, onClick = { selectedPost = post })
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    selectedPost?.let { post ->
                        PostDetailContent(
                            mainViewModel = mainViewModel,
                            post = post
                        )
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(mainViewModel.posts) { post ->
                    PostItem(mainViewModel = mainViewModel, post = post, onClick = { onPostClick(post.id) })
                }
            }
        }
    }
}

@Composable
fun PostItem(mainViewModel: MainViewModel, post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.imageUrl.replace("http://", "https://"),
                contentDescription = "Post image",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ubicación: ${post.location}", // Changed from latitude to location
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Publicado por: ${post.user?.email ?: "Usuario desconocido"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { mainViewModel.updatePostScore(post.id, 1) }) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Like")
                }
                Text(text = "${post.scores.sumOf { it.value }}")
                IconButton(onClick = { mainViewModel.updatePostScore(post.id, -1) }) {
                    Icon(Icons.Default.ThumbDown, contentDescription = "Dislike")
                }
            }
        }
    }
}

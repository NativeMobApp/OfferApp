package com.example.OfferApp.view.main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigateToCreatePost: () -> Unit,
    onPostClick: (Int) -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var selectedPostIndex by remember { mutableStateOf<Int?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val onQueryChangeAction: (String) -> Unit = { newQuery ->
        searchQuery = newQuery
        mainViewModel.searchPosts(newQuery)
    }
    Scaffold(
        topBar = {
            Header(
                query = searchQuery,
                onQueryChange = onQueryChangeAction,
                onSesionClicked = onLogoutClicked,
                onLogoClicked = { /* TODO: acción clic logo */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreatePost) {
                Icon(Icons.Default.Add, contentDescription = "Add post")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (isLandscape) {
            Row(modifier = Modifier.padding(paddingValues)) {
                // Lista de posts
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        itemsIndexed(mainViewModel.posts) { index, post ->
                            PostItem(post = post, onClick = { selectedPostIndex = index })
                        }
                    }
                }

                // Detalle del post seleccionado
                Box(modifier = Modifier.weight(1f)) {
                    selectedPostIndex?.let {
                        PostDetailScreen(post = mainViewModel.posts[it])
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                itemsIndexed(mainViewModel.posts) { index, post ->
                    PostItem(post = post, onClick = { onPostClick(index) })
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = "Post image",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Latitud: ${post.latitude}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Publicado por: ${post.user.email}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

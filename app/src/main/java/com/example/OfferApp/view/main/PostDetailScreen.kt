package com.example.OfferApp.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.OfferApp.domain.entities.Comment
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.view.header.Header
import com.example.OfferApp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostDetailScreen(
    mainViewModel: MainViewModel,
    post: Post,
    onBackClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    LaunchedEffect(post.id) {
        mainViewModel.loadComments(post.id)
    }

    Scaffold(
        topBar = {
            Header(
                query = mainViewModel.searchQuery,
                onQueryChange = { mainViewModel.onSearchQueryChange(it) },
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
    val comments by mainViewModel.comments.collectAsState()
    var newCommentText by remember { mutableStateOf("") }
    val currentUserIsAuthor = mainViewModel.user.uid == post.user?.uid

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // The entire column is now scrollable
    ) {
        // --- Post Info Section ---
        PostInfoSection(mainViewModel, post)

        // --- Comments Section ---
        Divider(modifier = Modifier.padding(top = 16.dp))
        Text(
            "Comentarios",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (comments.isEmpty()) {
                Text(
                    "Aún no hay comentarios. ¡Sé el primero!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                comments.forEach { comment ->
                    CommentItem(comment)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // --- Add Comment Section ---
        if (!currentUserIsAuthor) {
            AddCommentSection(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                onSend = {
                    mainViewModel.addComment(post.id, newCommentText)
                    newCommentText = "" // Clear input
                }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp)) // Extra space at the bottom
    }
}

@Composable
private fun PostInfoSection(mainViewModel: MainViewModel, post: Post) {
    val currentUserIsAuthor = mainViewModel.user.uid == post.user?.uid
    val score = post.scores.sumOf { it.value }
    val scoreColor = when {
        score > 0 -> Color.Green
        score < 0 -> Color.Red
        else -> LocalContentColor.current
    }
    val userVote = post.scores.find { it.userId == mainViewModel.user.uid }?.value
    val likeColor = if (userVote == 1) Color.Green else LocalContentColor.current
    val dislikeColor = if (userVote == -1) Color.Red else LocalContentColor.current

    Column(
        modifier = Modifier.padding(16.dp),
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { mainViewModel.updatePostScore(post.id, 1) }, enabled = !currentUserIsAuthor) {
                Icon(Icons.Default.ThumbUp, contentDescription = "Like", tint = likeColor)
            }
            Text(
                text = "$score",
                color = scoreColor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { mainViewModel.updatePostScore(post.id, -1) }, enabled = !currentUserIsAuthor) {
                Icon(Icons.Default.ThumbDown, contentDescription = "Dislike", tint = dislikeColor)
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = comment.user?.email ?: "Anónimo", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            comment.timestamp?.let {
                Text(text = sdf.format(it), style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(text = comment.text)
    }
}

@Composable
private fun AddCommentSection(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Añadir un comentario...") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onSend, enabled = value.isNotBlank()) {
            Icon(Icons.Default.Send, contentDescription = "Enviar comentario")
        }
    }
}
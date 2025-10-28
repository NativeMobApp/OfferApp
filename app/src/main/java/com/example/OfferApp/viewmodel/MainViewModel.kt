package com.example.OfferApp.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.repository.PostRepository
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(val user: User) : ViewModel() {
    private val repository = PostRepository()

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    
    private var originalPosts by mutableStateOf<List<Post>>(emptyList())
    
    // The search query is now part of the ViewModel's state
    var searchQuery by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            repository.getPosts().collect { postList ->
                originalPosts = postList
                // When the original list updates, re-apply the current search
                onSearchQueryChange(searchQuery)
            }
        }
    }

    suspend fun addPost(description: String, imageUri: Uri, location: String, latitude: Double, longitude: Double): Result<Unit> {
        val post = Post(
            description = description,
            location = location,
            latitude = latitude,
            longitude = longitude,
            user = user
        )
        return repository.addPost(post, imageUri)
    }

    fun updatePostScore(postId: String, value: Int) {
        viewModelScope.launch {
            repository.updatePostScore(postId, user.uid, value)
        }
    }

    // This function now updates the state and performs the search
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        posts = if (newQuery.isBlank()) {
            originalPosts
        } else {
            originalPosts.filter {
                it.description.contains(newQuery, ignoreCase = true) || 
                it.location.contains(newQuery, ignoreCase = true)
            }
        }
    }

    fun getPostById(id: String): Post? {
        return originalPosts.find { it.id == id }
    }
}
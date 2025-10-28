package com.example.OfferApp.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.OfferApp.data.repository.PostRepository
import com.example.OfferApp.domain.entities.Comment
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(val user: User) : ViewModel() {
    private val repository = PostRepository()

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    
    private var originalPosts by mutableStateOf<List<Post>>(emptyList())
    
    var searchQuery by mutableStateOf("")
        private set

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getPosts().collect { postList ->
                originalPosts = postList
                onSearchQueryChange(searchQuery) // Re-apply search on data change
            }
        }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch {
            repository.getCommentsForPost(postId).collect {
                _comments.value = it
            }
        }
    }

    fun addComment(postId: String, text: String) {
        viewModelScope.launch {
            val comment = Comment(user = user, text = text)
            repository.addCommentToPost(postId, comment)
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
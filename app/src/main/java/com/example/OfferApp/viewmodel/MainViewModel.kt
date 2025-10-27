package com.example.OfferApp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.User

class MainViewModel(val user: User) : ViewModel() {
    private val _allPosts = mutableStateListOf<Post>()

    var posts by mutableStateOf(_allPosts.toList())
        private set

    fun addPost(description: String, imageUrl: String, location: String, latitude: Double, longitude: Double) {
        _allPosts.add(
            Post(
                description = description,
                imageUrl = imageUrl,
                location = location,
                latitude = latitude,
                longitude = longitude,
                user = user
            )
        )
        posts = _allPosts.toList()
    }

    fun searchPosts(query: String) {
        if (query.isBlank()) {

            posts = _allPosts.toList()
        } else {
            posts = _allPosts.filter {
                it.description.contains(query, ignoreCase = true)
            }
        }
    }
}

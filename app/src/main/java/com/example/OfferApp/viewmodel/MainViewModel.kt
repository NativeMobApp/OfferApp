package com.example.OfferApp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.User

class MainViewModel(val user: User) : ViewModel() {
    private val _posts = mutableStateListOf<Post>()

    val posts: List<Post> = _posts

    fun addPost(description: String, imageUrl: String, location: String, latitude: Double, longitude: Double) {
        _posts.add(
            Post(
                description = description,
                imageUrl = imageUrl,
                location = location,
                latitude = latitude,
                longitude = longitude,
                user = user
            )
        )
    }
}

package com.example.OfferApp.domain.entities

data class Post(
    val description: String,
    val imageUrl: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val user: User
)
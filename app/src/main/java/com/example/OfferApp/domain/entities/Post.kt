package com.example.OfferApp.domain.entities

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    var description: String = "",
    var imageUrl: String = "",
    var location: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var user: User? = null
)
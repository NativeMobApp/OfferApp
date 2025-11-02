package com.example.OfferApp.domain.entities

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Score(
    val userId: String = "",
    val value: Int = 0 // +1 for like, -1 for dislike
)

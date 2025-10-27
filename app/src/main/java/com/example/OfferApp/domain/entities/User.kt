package com.example.OfferApp.domain.entities

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String = "",
    var email: String = ""
)
package com.example.OfferApp.data.repository

import com.example.OfferApp.data.fireBase.FirebaseAuthService

class AuthRepository(private val firebaseService: FirebaseAuthService) {
    suspend fun login(email: String, password: String) = firebaseService.login(email, password)
    suspend fun register(email: String, password: String) = firebaseService.register(email, password)
    fun logout() = firebaseService.logout()
    fun currentUser() = firebaseService.currentUser()
}
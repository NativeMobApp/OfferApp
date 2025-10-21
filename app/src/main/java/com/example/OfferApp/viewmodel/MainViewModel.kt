package com.example.OfferApp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.User

class MainViewModel(val user: User) : ViewModel() {
    private val _posts = mutableStateListOf(
        Post(
            description = "¡Gran oferta! Leche deslactosada LALA a solo $20.00 el litro. Válido hasta el 30 de junio.",
            imageUrl = "https://via.placeholder.com/300/FFC107/000000?Text=Oferta+Leche",
            location = "Supermercado La Confianza, Av. Siempre Viva 123",
            latitude = 19.4326,
            longitude = -99.1332,
            user = User(uid = "0", email = "Usuario de ejemplo")
        ),
        Post(
            description = "Pan de muerto recién horneado, ¡tradicional y delicioso! Pieza a $15.00.",
            imageUrl = "https://via.placeholder.com/300/E91E63/FFFFFF?Text=Pan+de+Muerto",
            location = "Panadería El Buen Sabor, Calle Falsa 456",
            latitude = 19.4355,
            longitude = -99.1365,
            user = User(uid = "0", email = "Usuario de ejemplo")
        ),
        Post(
            description = "Aprovecha el 3x2 en todas las galletas Marías. ¡No te quedes sin las tuyas!",
            imageUrl = "https://via.placeholder.com/300/4CAF50/FFFFFF?Text=Galletas+3x2",
            location = "Mini Super La Esquinita, Esq. con Calle Luna",
            latitude = 19.4285,
            longitude = -99.1410,
            user = User(uid = "0", email = "Usuario de ejemplo")
        ),
        Post(
            description = "Aguacate Hass de primera calidad a $35.00 el kilo. ¡Ideal para tu guacamole!",
            imageUrl = "https://via.placeholder.com/300/8BC34A/000000?Text=Aguacate+Hass",
            location = "Mercado Municipal, Puesto 25",
            latitude = 19.4400,
            longitude = -99.1300,
            user = User(uid = "0", email = "Usuario de ejemplo")
        ),
        Post(
            description = "Refresco Coca-Cola 2L retornable a precio especial de $28.00.",
            imageUrl = "https://via.placeholder.com/300/F44336/FFFFFF?Text=Coca-Cola",
            location = "Tienda de abarrotes Don Pepe",
            latitude = 19.4250,
            longitude = -99.1350,
            user = User(uid = "0", email = "Usuario de ejemplo")
        )
    )

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

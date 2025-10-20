package com.example.OfferApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.OfferApp.ui.theme.MyApplicationTheme

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth



import androidx.navigation.compose.rememberNavController
import com.example.OfferApp.data.fireBase.FirebaseAuthService
import com.example.OfferApp.data.repository.AuthRepository
import com.example.OfferApp.navigation.NavGraph
import com.example.OfferApp.view.logIn.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel = LoginViewModel(AuthRepository(FirebaseAuthService()))

        setContent {
            val navController = rememberNavController()
            NavGraph(navController, loginViewModel)
        }
    }
}

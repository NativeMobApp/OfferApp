package com.example.OfferApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.OfferApp.navigation.NavGraph
import com.example.OfferApp.view.login.LogInScreen
import com.example.OfferApp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            NavGraph(navController, authViewModel)

            LogInScreen(
                viewModel = authViewModel,
                onSuccess = {
                    // Acá podés navegar a tu HomeScreen
                }
            )



        }
    }
}

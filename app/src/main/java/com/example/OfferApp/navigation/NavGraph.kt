package com.example.OfferApp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.OfferApp.view.home.HomeScreen
import com.example.OfferApp.view.login.LogInScreen
import com.example.OfferApp.view.register.RegisterScreen
import com.example.OfferApp.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: AuthViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LogInScreen(loginViewModel) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("home") { HomeScreen() }
        composable("register") {
            RegisterScreen(loginViewModel) {
                navController.navigate("home") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }

    }
}

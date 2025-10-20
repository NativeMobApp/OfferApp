package com.example.OfferApp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.OfferApp.view.home.HomeScreen
import com.example.OfferApp.view.logIn.LoginScreen
import com.example.OfferApp.view.logIn.LoginViewModel

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(loginViewModel) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("home") { HomeScreen() }
    }
}

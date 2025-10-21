package com.example.OfferApp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.OfferApp.view.home.HomeScreen
import com.example.OfferApp.view.login.LogInScreen
import com.example.OfferApp.view.register.RegisterScreen
import com.example.OfferApp.view.forgotpassword.ForgotPasswordScreen
import com.example.OfferApp.viewmodel.AuthViewModel


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
}

@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LogInScreen(authViewModel,
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotClick = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }

        composable(Screen.Register.route) {
            RegisterScreen(authViewModel) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }
            }
        }

        /*
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onPasswordReset = { navController.popBackStack() }
            )
        }*/
    }
}

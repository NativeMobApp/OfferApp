package com.example.OfferApp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.OfferApp.view.home.HomeScreen
import com.example.OfferApp.view.login.LogInScreen
import com.example.OfferApp.view.register.RegisterScreen
import com.example.OfferApp.view.forgotpassword.ForgotPasswordScreen
import com.example.OfferApp.view.header.Header
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

                // Llama a HomeScreen y define el contenido del 'header'
                HomeScreen(
                    header = {
                        Header(
                            onSearchClicked = {
                            },
                            onSesionClicked = {
                                // Acción de Sesión: Cerrar sesión y navegar a Login
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    // Eliminar la pila de pantallas anteriores
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            onLogoClicked = {
                                // Acción de Logo: Volver a la parte superior de la Home
                                navController.popBackStack(Screen.Home.route, inclusive = false)
                            }
                        )
                    }
                )
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

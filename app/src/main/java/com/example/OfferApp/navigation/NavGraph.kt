package com.example.OfferApp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.OfferApp.domain.entities.User
import com.example.OfferApp.view.forgotpassword.ForgotPasswordScreen
import com.example.OfferApp.view.login.LogInScreen
import com.example.OfferApp.view.main.CreatePostScreen
import com.example.OfferApp.view.main.MainScreen
import com.example.OfferApp.view.main.PostDetailScreen
import com.example.OfferApp.view.register.RegisterScreen
import com.example.OfferApp.viewmodel.AuthViewModel
import com.example.OfferApp.viewmodel.MainViewModel
import java.util.UUID
import com.example.OfferApp.view.main.MapScreen

// -----------------------------
// RUTAS DEFINIDAS CON SEALED CLASS
// -----------------------------
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")

    object Main : Screen("main/{userName}") {
        fun createRoute(userName: String) = "main/$userName"
    }

    object CreatePost : Screen("create_post")
    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: Int) = "post_detail/$postId"
    }
    object Map : Screen("map")
}

// -----------------------------
// FACTORY DEL MAINVIEWMODEL
// -----------------------------
class MainViewModelFactory(private val userName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val user = User(uid = UUID.randomUUID().toString(), email = userName)
            return MainViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// -----------------------------
// NAVEGACIÓN PRINCIPAL
// -----------------------------
@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // -------- LOGIN --------
        composable(Screen.Login.route) {
            LogInScreen(
                authViewModel,
                onSuccess = { userName ->
                    navController.navigate(Screen.Main.createRoute(userName)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotClick = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }
        
        // -------- REGISTER --------
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack() } // Vuelve a Login
            )
        }

        // -------- MAIN (pantalla principal con posts) --------
        composable(
            route = Screen.Main.route,
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(userName))

            MainScreen(
                mainViewModel = mainViewModel,
                onNavigateToCreatePost = { navController.navigate(Screen.CreatePost.route) },
                onPostClick = { postIndex ->
                    navController.navigate(Screen.PostDetail.createRoute(postIndex))
                },
                        onLogoutClicked = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {

                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onNavigateToMap = {
                    navController.navigate(Screen.Map.route)
                }
            )
        }

        // -------- CREAR POST --------
        composable(Screen.CreatePost.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Main.route)
            }
            val userName = parentEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(userName), viewModelStoreOwner = parentEntry)

            CreatePostScreen(
                mainViewModel = mainViewModel,
                onPostCreated = { navController.popBackStack() }
            )
        }

        // -------- DETALLE DE POST --------
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Main.route)
            }
            val userName = parentEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(userName), viewModelStoreOwner = parentEntry)

            val postId = backStackEntry.arguments?.getInt("postId")
            val post = postId?.let { mainViewModel.posts.getOrNull(it) }

            if (post != null) {
                PostDetailScreen(post = post)
            } else {
                navController.popBackStack()
            }
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onPasswordReset = { navController.popBackStack() }
            )
        }

          // -------- PANTALLA DE MAPA --------
        composable(Screen.Map.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                // Obtenemos el MainViewModel desde la ruta principal para acceder a los posts
                navController.getBackStackEntry(Screen.Main.route)
            }
            val userName = parentEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(userName), viewModelStoreOwner = parentEntry)

            MapScreen(
                mainViewModel = mainViewModel,
                onBackClicked = { navController.popBackStack() } // Para volver atrás
            )
        }
    }
}

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
import com.example.OfferApp.view.main.MapScreen

// -----------------------------
// RUTAS DEFINIDAS CON SEALED CLASS
// -----------------------------
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")

    object Main : Screen("main/{uid}/{email}") { // Route now accepts uid and email
        fun createRoute(uid: String, email: String) = "main/$uid/$email"
    }

    object CreatePost : Screen("create_post")
    object PostDetail : Screen("post_detail/{postId}") { // The route now expects a String ID
        fun createRoute(postId: String) = "post_detail/$postId"
    }
    object Map : Screen("map")
}

// -----------------------------
// FACTORY DEL MAINVIEWMODEL
// -----------------------------
class MainViewModelFactory(private val user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
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
                onSuccess = { uid, email -> // Receives uid and email from the login screen
                    navController.navigate(Screen.Main.createRoute(uid, email)) {
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
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val user = User(uid, email)
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(user))

            MainScreen(
                mainViewModel = mainViewModel,
                onNavigateToCreatePost = { navController.navigate(Screen.CreatePost.route) },
                onPostClick = { postId -> // The click now provides the String ID
                    navController.navigate(Screen.PostDetail.createRoute(postId))
                },
                onLogoutClicked = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
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
            val uid = parentEntry.arguments?.getString("uid") ?: ""
            val email = parentEntry.arguments?.getString("email") ?: ""
            val user = User(uid, email)
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(user), viewModelStoreOwner = parentEntry)

            CreatePostScreen(
                mainViewModel = mainViewModel,
                onPostCreated = { navController.popBackStack() }
            )
        }

        // -------- DETALLE DE POST --------
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType }) // Argument is now a String
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Main.route)
            }
             val uid = parentEntry.arguments?.getString("uid") ?: ""
            val email = parentEntry.arguments?.getString("email") ?: ""
            val user = User(uid, email)
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(user), viewModelStoreOwner = parentEntry)

            val postId = backStackEntry.arguments?.getString("postId")
            val post = postId?.let { mainViewModel.getPostById(it) } // Find the post by its unique ID

            if (post != null) {
                PostDetailScreen(
                    mainViewModel = mainViewModel,
                    post = post,
                    onBackClicked = { navController.popBackStack() },
                    onLogoutClicked = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                             popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
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
                navController.getBackStackEntry(Screen.Main.route)
            }
            val uid = parentEntry.arguments?.getString("uid") ?: ""
            val email = parentEntry.arguments?.getString("email") ?: ""
            val user = User(uid, email)
            val mainViewModel: MainViewModel =
                viewModel(factory = MainViewModelFactory(user), viewModelStoreOwner = parentEntry)

            MapScreen(
                mainViewModel = mainViewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}
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
import com.example.OfferApp.viewmodel.AuthViewModel
import com.example.OfferApp.viewmodel.MainViewModel
import java.util.UUID

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

@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LogInScreen(authViewModel,
                onSuccess = {
                    navController.navigate("main/User") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") },
                onForgotClick = { navController.navigate("forgot_password") }
            )
        }
        composable(
            route = "main/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(userName))

            MainScreen(
                mainViewModel = mainViewModel,
                onNavigateToCreatePost = { navController.navigate("create_post") },
                onPostClick = { postIndex ->
                    navController.navigate("post_detail/$postIndex")
                }
            )
        }
        composable("create_post") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main/{userName}")
            }
            val userName = parentEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(userName), viewModelStoreOwner = parentEntry)
            CreatePostScreen(mainViewModel = mainViewModel, onPostCreated = { navController.popBackStack() })
        }
        composable(
            route = "post_detail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("main/{userName}")
            }
            val userName = parentEntry.arguments?.getString("userName") ?: ""
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(userName), viewModelStoreOwner = parentEntry)

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


    }
}

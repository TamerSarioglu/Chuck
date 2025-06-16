package com.tamersarioglu.chuck.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tamersarioglu.chuck.data.datastore.UserPreferencesDataStore
import com.tamersarioglu.chuck.presentation.screen.JokesScreen
import com.tamersarioglu.chuck.presentation.screen.LoginScreen
import com.tamersarioglu.chuck.presentation.screen.RegisterScreen
import com.tamersarioglu.chuck.presentation.screen.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object JokesRoute

@Serializable
object SearchRoute

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    userPreferencesDataStore: UserPreferencesDataStore
){
    
    val isLoggedIn by userPreferencesDataStore.isLoggedIn.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(LoginRoute) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) JokesRoute else LoginRoute
    ){
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                },
                onLoginSuccess = {
                    navController.navigate(JokesRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(JokesRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<JokesRoute> {
            JokesScreen(
                onNavigateToSearch = {
                    navController.navigate(SearchRoute)
                }
            )
        }

        composable<SearchRoute> {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
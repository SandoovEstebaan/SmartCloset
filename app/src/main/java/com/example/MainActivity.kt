package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AddGarmentScreen
import com.example.ui.screens.ClosetScreen
import com.example.ui.screens.ColorWheelScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OutfitsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ClosetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: ClosetViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // 1. Splash Screen
                        composable("splash") {
                            SplashScreen(
                                onTimeout = {
                                    navController.navigate("login") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Login Screen
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToSignUp = {
                                    navController.navigate("signup")
                                }
                            )
                        }

                        // 3. Signup Screen
                        composable("signup") {
                            SignUpScreen(
                                viewModel = viewModel,
                                onRegisterSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 4. Dashboard Screen
                        composable("dashboard") {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigate = { dest ->
                                    navController.navigate(dest)
                                }
                            )
                        }

                        // 5. Mi Armario Screen (All Items)
                        composable("closet") {
                            ClosetScreen(
                                viewModel = viewModel,
                                filterOnlyFavorites = false,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToAddGarment = { navController.navigate("add_garment") }
                            )
                        }

                        // 6. Favoritos Screen (Filtered to isFavorite only)
                        composable("favorites") {
                            ClosetScreen(
                                viewModel = viewModel,
                                filterOnlyFavorites = true,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToAddGarment = {}
                            )
                        }

                        // 7. Inteligentes Outfits Coordinator
                        composable("outfits") {
                            OutfitsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 8. Círculo Cromático (Interactive Color Harmonies)
                        composable("color_wheel") {
                            ColorWheelScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 9. Profile Screen
                        composable("profile") {
                            ProfileScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 10. Settings Screen
                        composable("settings") {
                            SettingsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 11. Add Garment (Camera shutter capture input form)
                        composable("add_garment") {
                            AddGarmentScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

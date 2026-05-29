package com.example.bromoindah.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bromoindah.ui.screen.auth.login.LoginScreen
import com.example.bromoindah.ui.screen.auth.register.RegisterScreen
import com.example.bromoindah.ui.screen.booking.BookingScreen
import com.example.bromoindah.ui.screen.detail.DetailScreen
import com.example.bromoindah.ui.screen.history.HistoryScreen
import com.example.bromoindah.ui.screen.home.HomeScreen
import com.example.bromoindah.ui.screen.profile.ProfileScreen
import com.example.bromoindah.ui.screen.review.ReviewScreen
import com.example.bromoindah.ui.screen.admin.dashboard.AdminDashboardScreen
import com.example.bromoindah.ui.screen.admin.manage_wisata.ManageWisataScreen
import com.example.bromoindah.ui.screen.admin.confirm_booking.ConfirmBookingScreen
import com.example.bromoindah.ui.screen.admin.manage_wisata.AddEditWisataScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { wisataId ->
                    navController.navigate(Screen.Detail.createRoute(wisataId))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("wisataId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() },
                onBookingClick = { wisataId ->
                    navController.navigate(Screen.Booking.createRoute(wisataId))
                }
            )
        }
        composable(
            route = Screen.Booking.route,
            arguments = listOf(navArgument("wisataId") { type = NavType.StringType })
        ) {
            BookingScreen(
                onBackClick = { navController.popBackStack() },
                onBookingSuccess = {
                    navController.navigate(Screen.History.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToReview = { pesananId, wisataId ->
                    navController.navigate(Screen.Review.createRoute(pesananId, wisataId))
                }
            )
        }
        composable(
            route = Screen.Review.route,
            arguments = listOf(
                navArgument("pesananId") { type = NavType.StringType },
                navArgument("wisataId") { type = NavType.StringType }
            )
        ) {
            ReviewScreen(
                onBackClick = { navController.popBackStack() },
                onReviewSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToAdmin = {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }
        
        // Admin Screens
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onNavigateToManageWisata = {
                    navController.navigate(Screen.ManageWisata.route)
                },
                onNavigateToConfirmBooking = {
                    navController.navigate(Screen.ConfirmBooking.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.ManageWisata.route) {
            ManageWisataScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToAddWisata = {
                    navController.navigate(Screen.AddWisata.route)
                },
                onNavigateToEditWisata = { wisataId ->
                    navController.navigate(Screen.EditWisata.createRoute(wisataId))
                }
            )
        }
        composable(Screen.AddWisata.route) {
            AddEditWisataScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.EditWisata.route,
            arguments = listOf(navArgument("wisataId") { type = NavType.StringType })
        ) {
            AddEditWisataScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        composable(Screen.ConfirmBooking.route) {
            ConfirmBookingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

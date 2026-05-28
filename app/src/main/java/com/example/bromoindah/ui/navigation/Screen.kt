package com.example.bromoindah.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Detail : Screen("detail/{wisataId}") {
        fun createRoute(wisataId: String) = "detail/$wisataId"
    }
    object Booking : Screen("booking/{wisataId}") {
        fun createRoute(wisataId: String) = "booking/$wisataId"
    }
    object History : Screen("history")
    object Profile : Screen("profile")
    object Review : Screen("review/{pesananId}/{wisataId}") {
        fun createRoute(pesananId: String, wisataId: String) = "review/$pesananId/$wisataId"
    }
    object AdminDashboard : Screen("admin_dashboard")
    object ManageWisata : Screen("manage_wisata")
    object AddWisata : Screen("add_wisata")
    object EditWisata : Screen("edit_wisata/{wisataId}") {
        fun createRoute(wisataId: String) = "edit_wisata/$wisataId"
    }
    object ConfirmBooking : Screen("confirm_booking")
}

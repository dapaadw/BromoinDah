package com.example.bromoindah

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.bromoindah.ui.screen.admin.AddEditWisataScreen
import com.example.bromoindah.ui.screen.admin.AdminDashboardScreen
import com.example.bromoindah.ui.screen.admin.ManageOrdersScreen
import com.example.bromoindah.ui.screen.admin.ManageWisataScreen
import com.example.bromoindah.ui.screen.auth.LoginScreen
import com.example.bromoindah.ui.screen.auth.RegisterScreen
import com.example.bromoindah.ui.screen.booking.BookingScreen
import com.example.bromoindah.ui.screen.booking.PaymentScreen
import com.example.bromoindah.ui.screen.history.OrderHistoryScreen
import com.example.bromoindah.ui.screen.home.HomeScreen
import com.example.bromoindah.ui.screen.profile.ProfileScreen
import com.example.bromoindah.ui.screen.review.ReviewScreen
import com.example.bromoindah.ui.screen.wisata.DetailWisataScreen
import com.example.bromoindah.ui.viewmodel.AuthViewModel
import com.example.bromoindah.ui.viewmodel.PesananViewModel
import com.example.bromoindah.ui.viewmodel.ReviewViewModel
import com.example.bromoindah.ui.viewmodel.WisataViewModel

@Composable
fun MainNavigation() {
    val authViewModel: AuthViewModel = viewModel()
    val wisataViewModel: WisataViewModel = viewModel()
    val pesananViewModel: PesananViewModel = viewModel()
    val reviewViewModel: ReviewViewModel = viewModel()

    val authState by authViewModel.uiState.collectAsState()

    // Initialize the back stack starting at Login
    val backStack = rememberNavBackStack(Login)

    // Handle authentication state changes to dynamically reroute
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            // Reroute to Home if user is logged in but stuck on auth screens
            if (backStack.contains(Login) || backStack.contains(Register)) {
                backStack.clear()
                backStack.add(Home)
            }
        } else {
            // Reroute to Login if user is logged out
            if (!backStack.contains(Login)) {
                backStack.clear()
                backStack.add(Login)
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            // Authentication: Login Screen
            entry<Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToRegister = {
                        backStack.add(Register)
                    },
                    authViewModel = authViewModel
                )
            }

            // Authentication: Register Screen
            entry<Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(Login)
                    },
                    authViewModel = authViewModel
                )
            }

            // Core Tab: Home / Beranda
            entry<Home> {
                HomeScreen(
                    onWisataClick = { id ->
                        backStack.add(DetailWisata(id))
                    },
                    onNavigateToHistory = {
                        backStack.clear()
                        backStack.add(OrderHistory)
                    },
                    onNavigateToProfile = {
                        backStack.clear()
                        backStack.add(Profile)
                    },
                    onNavigateToAdmin = {
                        backStack.clear()
                        backStack.add(AdminDashboard)
                    },
                    wisataViewModel = wisataViewModel,
                    userName = authState.profile?.fullName ?: "Pengguna",
                    isAdmin = authState.isAdmin,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Core Tab: Riwayat Pesanan
            entry<OrderHistory> {
                OrderHistoryScreen(
                    userId = authState.profile?.id ?: "",
                    isAdmin = authState.isAdmin,
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToHistory = {
                        // Already here
                    },
                    onNavigateToProfile = {
                        backStack.clear()
                        backStack.add(Profile)
                    },
                    onNavigateToAdmin = {
                        backStack.clear()
                        backStack.add(AdminDashboard)
                    },
                    onPayNavigate = { pesananId, totalHarga ->
                        backStack.add(Payment(pesananId, totalHarga))
                    },
                    onReviewNavigate = { pesananId, wisataId ->
                        backStack.add(Review(pesananId, wisataId))
                    },
                    pesananViewModel = pesananViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Core Tab: Profil Pengguna
            entry<Profile> {
                ProfileScreen(
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToHistory = {
                        backStack.clear()
                        backStack.add(OrderHistory)
                    },
                    onNavigateToProfile = {
                        // Already here
                    },
                    onNavigateToAdmin = {
                        backStack.clear()
                        backStack.add(AdminDashboard)
                    },
                    onLogoutSuccess = {
                        backStack.clear()
                        backStack.add(Login)
                    },
                    authViewModel = authViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Wisata: Detail Wisata
            entry<DetailWisata> { key ->
                DetailWisataScreen(
                    wisataId = key.wisataId,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onBookClick = { wId, name, price, imageUrl ->
                        backStack.add(Booking(wId, name, price, imageUrl))
                    },
                    wisataViewModel = wisataViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Pesanan: Booking Form
            entry<Booking> { key ->
                BookingScreen(
                    wisataId = key.wisataId,
                    wisataNama = key.wisataNama,
                    wisataHargaTiket = key.wisataHargaTiket,
                    wisataImageUrl = key.wisataImageUrl,
                    userId = authState.profile?.id ?: "",
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onPaymentNavigate = { pesananId, totalHarga ->
                        // Switch from booking screen directly to payment screen
                        backStack.removeLastOrNull()
                        backStack.add(Payment(pesananId, totalHarga))
                    },
                    pesananViewModel = pesananViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Pesanan: Payment Screen (Upload Proof)
            entry<Payment> { key ->
                PaymentScreen(
                    pesananId = key.pesananId,
                    totalHarga = key.totalHarga,
                    onNavigateBack = {
                        // Redirect to history page upon completion/back
                        backStack.clear()
                        backStack.add(OrderHistory)
                    },
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    pesananViewModel = pesananViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Review: Submit Rating & Review
            entry<Review> { key ->
                ReviewScreen(
                    pesananId = key.pesananId,
                    wisataId = key.wisataId,
                    userId = authState.profile?.id ?: "",
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    reviewViewModel = reviewViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Admin: Dashboard
            entry<AdminDashboard> {
                AdminDashboardScreen(
                    onNavigateToManageWisata = {
                        backStack.add(ManageWisata)
                    },
                    onNavigateToManageOrders = {
                        backStack.add(ManageOrders)
                    },
                    onNavigateBack = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Admin: Manage Wisata List
            entry<ManageWisata> {
                ManageWisataScreen(
                    onAddWisata = { id ->
                        backStack.add(AddEditWisata(id))
                    },
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    wisataViewModel = wisataViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Admin: Add or Edit Wisata Form
            entry<AddEditWisata> { key ->
                AddEditWisataScreen(
                    wisataId = key.wisataId,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    wisataViewModel = wisataViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Admin: Manage Orders (Confirm payment)
            entry<ManageOrders> {
                ManageOrdersScreen(
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    pesananViewModel = pesananViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

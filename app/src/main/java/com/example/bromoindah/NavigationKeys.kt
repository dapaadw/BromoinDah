package com.example.bromoindah

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Login : NavKey
@Serializable data object Register : NavKey
@Serializable data object Home : NavKey
@Serializable data class DetailWisata(val wisataId: String) : NavKey
@Serializable data class Booking(
    val wisataId: String,
    val wisataNama: String,
    val wisataHargaTiket: Int,
    val wisataImageUrl: String
) : NavKey
@Serializable data class Payment(
    val pesananId: String,
    val totalHarga: Int
) : NavKey
@Serializable data object Profile : NavKey
@Serializable data object OrderHistory : NavKey
@Serializable data class Review(
    val pesananId: String,
    val wisataId: String
) : NavKey
@Serializable data object AdminDashboard : NavKey
@Serializable data object ManageWisata : NavKey
@Serializable data class AddEditWisata(val wisataId: String? = null) : NavKey
@Serializable data object ManageOrders : NavKey

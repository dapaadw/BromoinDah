package com.example.bromoindah.domain.repository

import com.example.bromoindah.domain.model.Pesanan
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    suspend fun createBooking(pesanan: Pesanan): Result<Unit>
    fun getBookingsByUserId(userId: String): Flow<List<Pesanan>>
    suspend fun uploadPaymentProof(bookingId: String, byteArray: ByteArray): Result<String>
    suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit>
    fun getAllPendingBookings(): Flow<List<Pesanan>>
}

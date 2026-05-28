package com.example.bromoindah.data.repository

import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.BookingRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage
) : BookingRepository {

    override suspend fun createBooking(pesanan: Pesanan): Result<Unit> {
        return try {
            postgrest["pesanan"].insert(pesanan)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getBookingsByUserId(userId: String): Flow<List<Pesanan>> = flow {
        val result = postgrest["pesanan"]
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<Pesanan>()
        emit(result)
    }

    override suspend fun uploadPaymentProof(bookingId: String, byteArray: ByteArray): Result<String> {
        return try {
            val bucket = storage.from("payment-proofs")
            val fileName = "proof_$bookingId.jpg"
            bucket.upload(fileName, byteArray) {
                upsert = true
            }
            val publicUrl = bucket.publicUrl(fileName)
            
            postgrest["pesanan"].update({
                set("bukti_bayar_url", publicUrl)
                set("status_pesanan", "Menunggu Konfirmasi")
            }) {
                filter {
                    eq("id", bookingId)
                }
            }
            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit> {
        return try {
            postgrest["pesanan"].update({
                set("status_pesanan", status)
            }) {
                filter {
                    eq("id", bookingId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllPendingBookings(): Flow<List<Pesanan>> = flow {
        val result = postgrest["pesanan"]
            .select {
                filter {
                    eq("status_pesanan", "Menunggu Konfirmasi")
                }
            }
            .decodeList<Pesanan>()
        emit(result)
    }
}

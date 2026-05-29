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
            .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("*, wisata(nama_wisata)")) {
                filter {
                    eq("user_id", userId)
                }
            }
        
        val bookings = result.decodeList<Pesanan>().mapIndexed { index, pesanan ->
            val wisataName = result.decodeList<kotlinx.serialization.json.JsonObject>()[index]["wisata"]?.let {
                if (it is kotlinx.serialization.json.JsonObject) {
                    it["nama_wisata"]?.let { name ->
                        if (name is kotlinx.serialization.json.JsonPrimitive) name.content else null
                    }
                } else null
            }
            pesanan.copy(wisata_name = wisataName)
        }
        emit(bookings)
    }

    override suspend fun uploadPaymentProof(bookingId: String, byteArray: ByteArray): Result<String> {
        return try {
            val bucket = storage.from("payment-proofs")
            val fileName = "proof_$bookingId.jpg"
            
            // 1. Upload the file to Storage
            bucket.upload(fileName, byteArray) {
                upsert = true
            }
            
            // 2. Get the public URL
            val publicUrl = bucket.publicUrl(fileName)
            
            // 3. Update the database record
            postgrest.from("pesanan").update({
                set("bukti_bayar_url", publicUrl)
                set("status_pesanan", "Menunggu Konfirmasi")
            }) {
                filter {
                    eq("id", bookingId)
                }
            }
            
            Result.success(publicUrl)
        } catch (e: Exception) {
            e.printStackTrace()
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

    override fun getAllConfirmedBookings(): Flow<List<Pesanan>> = flow {
        val result = postgrest["pesanan"]
            .select {
                filter {
                    eq("status_pesanan", "Dikonfirmasi")
                }
            }
            .decodeList<Pesanan>()
        emit(result)
    }
}

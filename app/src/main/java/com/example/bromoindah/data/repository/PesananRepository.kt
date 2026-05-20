package com.example.bromoindah.data.repository

import com.example.bromoindah.data.SupabaseClient
import com.example.bromoindah.data.model.Pesanan
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage

object PesananRepository {
    private val client = SupabaseClient.client

    suspend fun createPesanan(pesanan: Pesanan): Result<Unit> {
        return try {
            client.postgrest.from("pesanan").insert(pesanan)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPesananByUser(userId: String): Result<List<Pesanan>> {
        return try {
            val pesananList = client.postgrest.from("pesanan")
                .select(Columns.raw("*, wisata(*)")) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Pesanan>()
            Result.success(pesananList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPesanan(): Result<List<Pesanan>> {
        return try {
            val pesananList = client.postgrest.from("pesanan")
                .select(Columns.raw("*, wisata(*)")) {
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Pesanan>()
            Result.success(pesananList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePesananStatus(pesananId: String, status: String): Result<Unit> {
        return try {
            client.postgrest.from("pesanan").update(
                {
                    set("status", status)
                }
            ) {
                filter {
                    eq("id", pesananId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadBuktiBayar(pesananId: String, fileName: String, bytes: ByteArray): Result<Unit> {
        return try {
            client.storage.from("bukti-bayar").upload(fileName, bytes)
            val publicUrl = client.storage.from("bukti-bayar").publicUrl(fileName)
            updateBuktiBayar(pesananId, publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBuktiBayar(pesananId: String, url: String): Result<Unit> {
        return try {
            client.postgrest.from("pesanan").update(
                {
                    set("bukti_bayar_url", url)
                }
            ) {
                filter {
                    eq("id", pesananId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

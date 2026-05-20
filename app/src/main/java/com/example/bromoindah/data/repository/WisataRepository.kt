package com.example.bromoindah.data.repository

import com.example.bromoindah.data.SupabaseClient
import com.example.bromoindah.data.model.Wisata
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage

object WisataRepository {
    private val client = SupabaseClient.client

    suspend fun getAllWisata(): Result<List<Wisata>> {
        return try {
            val wisataList = client.postgrest.from("wisata")
                .select()
                .decodeList<Wisata>()
            Result.success(wisataList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWisataById(id: String): Result<Wisata> {
        return try {
            val wisata = client.postgrest.from("wisata")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Wisata>()
            Result.success(wisata)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addWisata(wisata: Wisata): Result<Unit> {
        return try {
            client.postgrest.from("wisata").insert(wisata)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateWisata(wisata: Wisata): Result<Unit> {
        return try {
            client.postgrest.from("wisata").update(wisata) {
                filter {
                    eq("id", wisata.id.orEmpty())
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteWisata(id: String): Result<Unit> {
        return try {
            client.postgrest.from("wisata").delete {
                filter {
                    eq("id", id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(fileName: String, bytes: ByteArray): Result<String> {
        return try {
            client.storage.from("wisata-images").upload(fileName, bytes)
            val publicUrl = client.storage.from("wisata-images").publicUrl(fileName)
            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

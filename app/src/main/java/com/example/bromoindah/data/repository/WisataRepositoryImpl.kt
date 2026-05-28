package com.example.bromoindah.data.repository

import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WisataRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : WisataRepository {

    override fun getWisataList(): Flow<List<Wisata>> = flow {
        val result = postgrest["wisata"]
            .select()
            .decodeList<Wisata>()
        emit(result)
    }

    override suspend fun getWisataById(id: String): Wisata? {
        return try {
            postgrest["wisata"]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<Wisata>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchWisata(query: String): List<Wisata> {
        return postgrest["wisata"]
            .select {
                filter {
                    ilike("nama_wisata", "%$query%")
                }
            }
            .decodeList<Wisata>()
    }

    override suspend fun upsertWisata(wisata: Wisata): Result<Unit> {
        return try {
            postgrest["wisata"].upsert(wisata)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWisata(id: String): Result<Unit> {
        return try {
            postgrest["wisata"].delete {
                filter {
                    eq("id", id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

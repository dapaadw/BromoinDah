package com.example.bromoindah.data.repository

import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WisataRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage
) : WisataRepository {

    // A trigger to refresh the list whenever data changes
    private val refreshTrigger = MutableStateFlow(System.currentTimeMillis())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWisataList(): Flow<List<Wisata>> = refreshTrigger.flatMapLatest {
        flow {
            val wisataResult = postgrest.from("wisata").select()
            val wisataList = wisataResult.decodeList<Wisata>()
            
            val reviewsResult = postgrest.from("review").select()
            val allReviews = reviewsResult.decodeList<com.example.bromoindah.domain.model.Review>()
            
            val result = wisataList.map { wisata ->
                val reviewsForWisata = allReviews.filter { it.wisata_id == wisata.id }
                val avgRating = if (reviewsForWisata.isNotEmpty()) {
                    reviewsForWisata.map { it.rating }.average()
                } else 0.0
                wisata.copy(
                    average_rating = avgRating,
                    total_reviews = reviewsForWisata.size
                )
            }
            emit(result)
        }
    }

    override suspend fun getWisataById(id: String): Wisata? {
        return try {
            val wisata = postgrest.from("wisata")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<Wisata>()
                
            if (wisata != null) {
                val reviews = postgrest.from("review")
                    .select {
                        filter {
                            eq("wisata_id", id)
                        }
                    }
                    .decodeList<com.example.bromoindah.domain.model.Review>()
                
                val avgRating = if (reviews.isNotEmpty()) {
                    reviews.map { it.rating }.average()
                } else 0.0
                
                wisata.copy(
                    average_rating = avgRating,
                    total_reviews = reviews.size
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchWisata(query: String): List<Wisata> {
        return postgrest.from("wisata")
            .select {
                filter {
                    ilike("nama_wisata", "%$query%")
                }
            }
            .decodeList<Wisata>()
    }

    override suspend fun upsertWisata(wisata: Wisata): Result<Unit> {
        return try {
            postgrest.from("wisata").upsert(wisata)
            // Trigger refresh
            refreshTrigger.value = System.currentTimeMillis()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWisata(id: String): Result<Unit> {
        return try {
            postgrest.from("wisata").delete {
                filter {
                    eq("id", id)
                }
            }
            // Trigger refresh
            refreshTrigger.value = System.currentTimeMillis()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadWisataImage(byteArray: ByteArray, fileName: String): Result<String> {
        return try {
            val bucket = storage.from("wisata-images")
            bucket.upload(fileName, byteArray) {
                upsert = true
            }
            Result.success(bucket.publicUrl(fileName))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

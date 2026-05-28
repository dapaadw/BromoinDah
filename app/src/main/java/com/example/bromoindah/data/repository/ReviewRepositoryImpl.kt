package com.example.bromoindah.data.repository

import com.example.bromoindah.domain.model.Review
import com.example.bromoindah.domain.repository.ReviewRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage
) : ReviewRepository {

    override fun getReviewsByWisataId(wisataId: String): Flow<List<Review>> = flow {
        val result = postgrest["review"]
            .select {
                filter {
                    eq("wisata_id", wisataId)
                }
            }
            .decodeList<Review>()
        emit(result)
    }

    override suspend fun createReview(review: Review, byteArray: ByteArray?): Result<Unit> {
        return try {
            var finalReview = review
            if (byteArray != null) {
                val bucket = storage.from("review-photos")
                val fileName = "review_${review.pesanan_id}.jpg"
                bucket.upload(fileName, byteArray) {
                    upsert = true
                }
                val publicUrl = bucket.publicUrl(fileName)
                finalReview = review.copy(foto_review_url = publicUrl)
            }
            
            postgrest["review"].insert(finalReview)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

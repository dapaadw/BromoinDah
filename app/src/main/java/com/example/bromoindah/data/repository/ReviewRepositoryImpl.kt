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
            .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("*, users(nama_lengkap)")) {
                filter {
                    eq("wisata_id", wisataId)
                }
            }
        
        val reviews = result.decodeList<Review>().mapIndexed { index, review ->
            val userName = result.decodeList<kotlinx.serialization.json.JsonObject>()[index]["users"]?.let { 
                if (it is kotlinx.serialization.json.JsonObject) {
                    it["nama_lengkap"]?.let { name ->
                        if (name is kotlinx.serialization.json.JsonPrimitive) name.content else null
                    }
                } else null
            }
            review.copy(user_name = userName)
        }
        emit(reviews)
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
            
            // Update the booking to mark it as reviewed
            postgrest["pesanan"].update({
                set("is_reviewed", true)
            }) {
                filter {
                    eq("id", review.pesanan_id)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

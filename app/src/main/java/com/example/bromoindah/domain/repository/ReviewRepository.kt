package com.example.bromoindah.domain.repository

import com.example.bromoindah.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviewsByWisataId(wisataId: String): Flow<List<Review>>
    suspend fun createReview(review: Review, byteArray: ByteArray?): Result<Unit>
}

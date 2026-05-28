package com.example.bromoindah.domain.usecase.review

import com.example.bromoindah.domain.model.Review
import com.example.bromoindah.domain.repository.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(review: Review, byteArray: ByteArray? = null): Result<Unit> {
        if (review.rating < 1 || review.rating > 5) {
            return Result.failure(Exception("Rating must be between 1 and 5"))
        }
        return repository.createReview(review, byteArray)
    }
}

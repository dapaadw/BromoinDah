package com.example.bromoindah.domain.usecase.review

import com.example.bromoindah.domain.model.Review
import com.example.bromoindah.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsByWisataIdUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(wisataId: String): Flow<List<Review>> {
        return repository.getReviewsByWisataId(wisataId)
    }
}

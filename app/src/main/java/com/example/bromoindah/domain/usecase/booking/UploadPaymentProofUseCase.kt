package com.example.bromoindah.domain.usecase.booking

import com.example.bromoindah.domain.repository.BookingRepository
import javax.inject.Inject

class UploadPaymentProofUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String, byteArray: ByteArray): Result<String> {
        return repository.uploadPaymentProof(bookingId, byteArray)
    }
}

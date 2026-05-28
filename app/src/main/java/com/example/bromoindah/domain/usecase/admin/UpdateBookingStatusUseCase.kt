package com.example.bromoindah.domain.usecase.admin

import com.example.bromoindah.domain.repository.BookingRepository
import javax.inject.Inject

class UpdateBookingStatusUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String, status: String): Result<Unit> {
        return repository.updateBookingStatus(bookingId, status)
    }
}

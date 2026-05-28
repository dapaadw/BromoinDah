package com.example.bromoindah.domain.usecase.booking

import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(pesanan: Pesanan): Result<Unit> {
        return repository.createBooking(pesanan)
    }
}

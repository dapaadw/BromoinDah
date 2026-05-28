package com.example.bromoindah.domain.usecase.booking

import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(userId: String): Flow<List<Pesanan>> {
        return repository.getBookingsByUserId(userId)
    }
}

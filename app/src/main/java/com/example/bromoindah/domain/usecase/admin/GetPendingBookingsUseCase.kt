package com.example.bromoindah.domain.usecase.admin

import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(): Flow<List<Pesanan>> = repository.getAllPendingBookings()
}

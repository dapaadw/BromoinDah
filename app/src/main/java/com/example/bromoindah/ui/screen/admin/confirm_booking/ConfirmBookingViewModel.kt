package com.example.bromoindah.ui.screen.admin.confirm_booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.usecase.admin.GetPendingBookingsUseCase
import com.example.bromoindah.domain.usecase.admin.UpdateBookingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmBookingViewModel @Inject constructor(
    private val getPendingBookingsUseCase: GetPendingBookingsUseCase,
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmBookingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPendingBookings()
    }

    fun getPendingBookings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPendingBookingsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, bookings = list) }
                }
        }
    }

    fun updateStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            updateBookingStatusUseCase(bookingId, status)
                .onSuccess {
                    getPendingBookings()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}

data class ConfirmBookingUiState(
    val bookings: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

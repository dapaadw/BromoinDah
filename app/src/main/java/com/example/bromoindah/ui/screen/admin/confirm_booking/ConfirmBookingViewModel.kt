package com.example.bromoindah.ui.screen.admin.confirm_booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.usecase.admin.GetConfirmedBookingsUseCase
import com.example.bromoindah.domain.usecase.admin.GetPendingBookingsUseCase
import com.example.bromoindah.domain.usecase.admin.UpdateBookingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmBookingViewModel @Inject constructor(
    private val getPendingBookingsUseCase: GetPendingBookingsUseCase,
    private val getConfirmedBookingsUseCase: GetConfirmedBookingsUseCase,
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmBookingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val pendingFlow = getPendingBookingsUseCase().catch { emit(emptyList()) }
            val confirmedFlow = getConfirmedBookingsUseCase().catch { emit(emptyList()) }

            combine(pendingFlow, confirmedFlow) { pending, confirmed ->
                pending to confirmed
            }.collect { (pending, confirmed) ->
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        pendingBookings = pending,
                        confirmedBookings = confirmed
                    ) 
                }
            }
        }
    }

    fun updateStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            updateBookingStatusUseCase(bookingId, status)
                .onSuccess {
                    loadData()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}

data class ConfirmBookingUiState(
    val pendingBookings: List<Pesanan> = emptyList(),
    val confirmedBookings: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

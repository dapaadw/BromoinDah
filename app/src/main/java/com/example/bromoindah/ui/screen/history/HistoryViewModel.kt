package com.example.bromoindah.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.AuthRepository
import com.example.bromoindah.domain.usecase.booking.GetBookingsUseCase
import com.example.bromoindah.domain.usecase.booking.UploadPaymentProofUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val uploadPaymentProofUseCase: UploadPaymentProofUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getBookings()
    }

    fun getBookings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val currentUser = authRepository.getCurrentUser().firstOrNull()
            if (currentUser != null) {
                getBookingsUseCase(currentUser.id)
                    .catch { e ->
                        _uiState.update { it.copy(isLoading = false, error = "Gagal memuat: ${e.message}") }
                    }
                    .collect { list ->
                        _uiState.update { it.copy(isLoading = false, bookings = list) }
                    }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Sesi berakhir. Silakan login kembali.") }
            }
        }
    }

    fun uploadPaymentProof(bookingId: String, byteArray: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            uploadPaymentProofUseCase(bookingId, byteArray)
                .onSuccess {
                    // Force a refresh of the list to show the new status
                    getBookings()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Gagal upload: ${e.message}") }
                }
        }
    }
}

data class HistoryUiState(
    val bookings: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

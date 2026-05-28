package com.example.bromoindah.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.repository.AuthRepository
import com.example.bromoindah.domain.usecase.booking.GetBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getBookings()
    }

    fun getBookings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = authRepository.getCurrentUser().firstOrNull()
            if (currentUser != null) {
                getBookingsUseCase(currentUser.id)
                    .catch { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                    }
                    .collect { list ->
                        _uiState.update { it.copy(isLoading = false, bookings = list) }
                    }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Sesi berakhir") }
            }
        }
    }
}

data class HistoryUiState(
    val bookings: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.example.bromoindah.ui.screen.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Pesanan
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.usecase.booking.CreateBookingUseCase
import com.example.bromoindah.domain.usecase.wisata.GetWisataByIdUseCase
import com.example.bromoindah.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getWisataByIdUseCase: GetWisataByIdUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wisataId: String? = savedStateHandle["wisataId"]

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWisata()
    }

    private fun getWisata() {
        wisataId?.let { id ->
            viewModelScope.launch {
                val wisata = getWisataByIdUseCase(id)
                _uiState.update { it.copy(wisata = wisata) }
            }
        }
    }

    fun onDateSelected(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onJumlahTiketChanged(jumlah: Int) {
        val total = (_uiState.value.wisata?.harga_tiket ?: 0) * jumlah
        _uiState.update { it.copy(jumlahTiket = jumlah, totalHarga = total) }
    }

    fun createBooking() {
        val wisata = _uiState.value.wisata ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val currentUser = authRepository.getCurrentUser().firstOrNull()
            if (currentUser == null) {
                _uiState.update { it.copy(isLoading = false, error = "Sesi berakhir, silakan login kembali") }
                return@launch
            }

            val pesanan = Pesanan(
                user_id = currentUser.id,
                wisata_id = wisata.id ?: "",
                tanggal_pesan = LocalDate.parse(_uiState.value.selectedDate).atStartOfDayIn(TimeZone.UTC),
                jumlah_tiket = _uiState.value.jumlahTiket,
                total_harga = _uiState.value.totalHarga
            )

            createBookingUseCase(pesanan).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

data class BookingUiState(
    val wisata: Wisata? = null,
    val selectedDate: String = "",
    val jumlahTiket: Int = 1,
    val totalHarga: Int = 0,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

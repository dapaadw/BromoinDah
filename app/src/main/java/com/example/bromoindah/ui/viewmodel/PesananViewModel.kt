package com.example.bromoindah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.data.model.Pesanan
import com.example.bromoindah.data.repository.PesananRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PesananUiState(
    val pesananList: List<Pesanan> = emptyList(),
    val allPesanan: List<Pesanan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderSuccess: Boolean = false
)

class PesananViewModel : ViewModel() {
    private val repo = PesananRepository

    private val _uiState = MutableStateFlow(PesananUiState())
    val uiState: StateFlow<PesananUiState> = _uiState.asStateFlow()

    fun createPesanan(
        wisataId: String,
        userId: String,
        tanggalKunjungan: String,
        jumlahTiket: Int,
        totalHarga: Int,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val orderId = java.util.UUID.randomUUID().toString()
            val pesanan = Pesanan(
                id = orderId,
                userId = userId,
                wisataId = wisataId,
                tanggalKunjungan = tanggalKunjungan,
                jumlahTiket = jumlahTiket,
                totalHarga = totalHarga,
                status = "pending"
            )
            repo.createPesanan(pesanan).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orderSuccess = true
                    )
                    onSuccess(orderId)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal membuat pesanan"
                    )
                }
            )
        }
    }

    fun loadUserPesanan(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.getPesananByUser(userId).fold(
                onSuccess = { list ->
                    _uiState.value = _uiState.value.copy(
                        pesananList = list,
                        isLoading = false
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memuat pesanan"
                    )
                }
            )
        }
    }

    fun loadAllPesanan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.getAllPesanan().fold(
                onSuccess = { list ->
                    _uiState.value = _uiState.value.copy(
                        allPesanan = list,
                        isLoading = false
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memuat semua pesanan"
                    )
                }
            )
        }
    }

    fun updateStatus(pesananId: String, status: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.updatePesananStatus(pesananId, status).fold(
                onSuccess = { loadAllPesanan() },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memperbarui status pesanan"
                    )
                }
            )
        }
    }

    fun uploadBuktiBayar(pesananId: String, fileName: String, bytes: ByteArray) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.uploadBuktiBayar(pesananId, fileName, bytes).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal mengunggah bukti pembayaran"
                    )
                }
            )
        }
    }

    fun resetOrderSuccess() {
        _uiState.value = _uiState.value.copy(orderSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

package com.example.bromoindah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.data.model.Review
import com.example.bromoindah.data.model.Wisata
import com.example.bromoindah.data.repository.ReviewRepository
import com.example.bromoindah.data.repository.WisataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WisataUiState(
    val wisataList: List<Wisata> = emptyList(),
    val selectedWisata: Wisata? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class WisataViewModel : ViewModel() {
    private val wisataRepo = WisataRepository
    private val reviewRepo = ReviewRepository

    private val _uiState = MutableStateFlow(WisataUiState())
    val uiState: StateFlow<WisataUiState> = _uiState.asStateFlow()

    private var allWisataList: List<Wisata> = emptyList()

    init {
        loadWisataList()
    }

    fun loadWisataList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            wisataRepo.getAllWisata().fold(
                onSuccess = { list ->
                    allWisataList = list
                    _uiState.value = _uiState.value.copy(
                        wisataList = list,
                        isLoading = false
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memuat data wisata"
                    )
                }
            )
        }
    }

    fun loadWisataDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            wisataRepo.getWisataById(id).fold(
                onSuccess = { wisata ->
                    _uiState.value = _uiState.value.copy(
                        selectedWisata = wisata,
                        isLoading = false
                    )
                    // Load reviews for this wisata
                    reviewRepo.getReviewsByWisata(id).fold(
                        onSuccess = { reviews ->
                            _uiState.value = _uiState.value.copy(reviews = reviews)
                        },
                        onFailure = { /* Reviews failed to load, non-critical */ }
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memuat detail wisata"
                    )
                }
            )
        }
    }

    fun addWisata(wisata: Wisata) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            wisataRepo.addWisata(wisata).fold(
                onSuccess = { loadWisataList() },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal menambahkan wisata"
                    )
                }
            )
        }
    }

    fun updateWisata(wisata: Wisata) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            wisataRepo.updateWisata(wisata).fold(
                onSuccess = { loadWisataList() },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal memperbarui wisata"
                    )
                }
            )
        }
    }

    fun deleteWisata(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            wisataRepo.deleteWisata(id).fold(
                onSuccess = { loadWisataList() },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal menghapus wisata"
                    )
                }
            )
        }
    }

    fun uploadImage(fileName: String, bytes: ByteArray, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            wisataRepo.uploadImage(fileName, bytes).fold(
                onSuccess = { url -> onResult(url) },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        error = it.message ?: "Gagal mengunggah gambar"
                    )
                    onResult(null)
                }
            )
        }
    }

    fun searchWisata(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(wisataList = allWisataList)
        } else {
            val filtered = allWisataList.filter { wisata ->
                wisata.nama.contains(query, ignoreCase = true) ||
                        wisata.lokasi.contains(query, ignoreCase = true)
            }
            _uiState.value = _uiState.value.copy(wisataList = filtered)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

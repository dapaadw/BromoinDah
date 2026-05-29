package com.example.bromoindah.ui.screen.admin.manage_wisata

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.usecase.admin.UpsertWisataUseCase
import com.example.bromoindah.domain.usecase.admin.UploadWisataImageUseCase
import com.example.bromoindah.domain.usecase.wisata.GetWisataByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditWisataViewModel @Inject constructor(
    private val upsertWisataUseCase: UpsertWisataUseCase,
    private val uploadWisataImageUseCase: UploadWisataImageUseCase,
    private val getWisataByIdUseCase: GetWisataByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wisataId: String? = savedStateHandle["wisataId"]

    private val _uiState = MutableStateFlow(AddEditWisataUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (wisataId != null) {
            loadWisata(wisataId)
        }
    }

    private fun loadWisata(id: String) {
        viewModelScope.launch {
            val wisata = getWisataByIdUseCase(id)
            if (wisata != null) {
                _uiState.update { 
                    it.copy(
                        id = wisata.id,
                        nama = wisata.nama_wisata,
                        deskripsi = wisata.deskripsi,
                        lokasi = wisata.lokasi,
                        harga = wisata.harga_tiket.toString(),
                        jamOperasional = wisata.jam_operasional ?: "",
                        fasilitas = wisata.fasilitas ?: "",
                        aturan = wisata.aturan_kunjungan ?: "",
                        fotoUrl = wisata.foto_wisata_url ?: ""
                    )
                }
            }
        }
    }

    fun onNamaChanged(value: String) = _uiState.update { it.copy(nama = value) }
    fun onDeskripsiChanged(value: String) = _uiState.update { it.copy(deskripsi = value) }
    fun onLokasiChanged(value: String) = _uiState.update { it.copy(lokasi = value) }
    fun onHargaChanged(value: String) = _uiState.update { it.copy(harga = value) }
    fun onJamOperasionalChanged(value: String) = _uiState.update { it.copy(jamOperasional = value) }
    fun onFasilitasChanged(value: String) = _uiState.update { it.copy(fasilitas = value) }
    fun onAturanChanged(value: String) = _uiState.update { it.copy(aturan = value) }
    
    fun onImageSelected(byteArray: ByteArray) {
        _uiState.update { it.copy(selectedImageBytes = byteArray) }
    }

    fun saveWisata() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            var currentFotoUrl = _uiState.value.fotoUrl
            
            // 1. Upload image if a new one is selected
            _uiState.value.selectedImageBytes?.let { bytes ->
                val fileName = "wisata_${UUID.randomUUID()}.jpg"
                uploadWisataImageUseCase(bytes, fileName).onSuccess { url ->
                    currentFotoUrl = url
                }.onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Gagal upload gambar: ${e.message}") }
                    return@launch
                }
            }

            // 2. Upsert the wisata data
            val wisata = Wisata(
                id = _uiState.value.id,
                nama_wisata = _uiState.value.nama,
                deskripsi = _uiState.value.deskripsi,
                lokasi = _uiState.value.lokasi,
                harga_tiket = _uiState.value.harga.toIntOrNull() ?: 0,
                jam_operasional = _uiState.value.jamOperasional,
                fasilitas = _uiState.value.fasilitas,
                aturan_kunjungan = _uiState.value.aturan,
                foto_wisata_url = currentFotoUrl
            )

            upsertWisataUseCase(wisata).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

data class AddEditWisataUiState(
    val id: String? = null,
    val nama: String = "",
    val deskripsi: String = "",
    val lokasi: String = "",
    val harga: String = "",
    val jamOperasional: String = "",
    val fasilitas: String = "",
    val aturan: String = "",
    val fotoUrl: String = "",
    val selectedImageBytes: ByteArray? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

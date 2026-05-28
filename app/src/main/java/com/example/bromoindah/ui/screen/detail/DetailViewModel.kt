package com.example.bromoindah.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.usecase.wisata.GetWisataByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWisataByIdUseCase: GetWisataByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wisataId: String? = savedStateHandle["wisataId"]

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWisata()
    }

    fun getWisata() {
        wisataId?.let { id ->
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val wisata = getWisataByIdUseCase(id)
                if (wisata != null) {
                    _uiState.update { it.copy(isLoading = false, wisata = wisata) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Wisata tidak ditemukan") }
                }
            }
        }
    }
}

data class DetailUiState(
    val wisata: Wisata? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

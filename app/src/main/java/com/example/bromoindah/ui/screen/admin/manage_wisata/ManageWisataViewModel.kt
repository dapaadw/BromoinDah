package com.example.bromoindah.ui.screen.admin.manage_wisata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageWisataViewModel @Inject constructor(
    private val repository: WisataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageWisataUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWisataList()
    }

    fun getWisataList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getWisataList()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, wisataList = list) }
                }
        }
    }

    fun deleteWisata(id: String) {
        viewModelScope.launch {
            repository.deleteWisata(id).onSuccess {
                getWisataList()
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}

data class ManageWisataUiState(
    val wisataList: List<Wisata> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

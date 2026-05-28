package com.example.bromoindah.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.usecase.wisata.GetWisataListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWisataListUseCase: GetWisataListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWisataList()
    }

    fun getWisataList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getWisataListUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, wisataList = list) }
                }
        }
    }
}

data class HomeUiState(
    val wisataList: List<Wisata> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

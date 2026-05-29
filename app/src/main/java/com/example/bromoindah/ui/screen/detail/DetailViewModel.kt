package com.example.bromoindah.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Review
import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.usecase.review.GetReviewsByWisataIdUseCase
import com.example.bromoindah.domain.usecase.wisata.GetWisataByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWisataByIdUseCase: GetWisataByIdUseCase,
    private val getReviewsByWisataIdUseCase: GetReviewsByWisataIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wisataId: String? = savedStateHandle["wisataId"]

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getWisata()
        getReviews()
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

    fun getReviews() {
        wisataId?.let { id ->
            getReviewsByWisataIdUseCase(id)
                .onEach { reviews ->
                    _uiState.update { it.copy(reviews = reviews) }
                }
                .launchIn(viewModelScope)
        }
    }
}

data class DetailUiState(
    val wisata: Wisata? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.example.bromoindah.ui.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.Review
import com.example.bromoindah.domain.repository.AuthRepository
import com.example.bromoindah.domain.usecase.review.CreateReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val createReviewUseCase: CreateReviewUseCase,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pesananId: String? = savedStateHandle["pesananId"]
    private val wisataId: String? = savedStateHandle["wisataId"]

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState = _uiState.asStateFlow()

    fun onRatingChanged(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun onUlasanChanged(ulasan: String) {
        _uiState.update { it.copy(ulasan = ulasan) }
    }

    fun submitReview(photoBytes: ByteArray? = null) {
        val pId = pesananId ?: return
        val wId = wisataId ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = authRepository.getCurrentUser().firstOrNull()
            if (currentUser == null) {
                _uiState.update { it.copy(isLoading = false, error = "Sesi berakhir") }
                return@launch
            }

            val review = Review(
                user_id = currentUser.id,
                wisata_id = wId,
                pesanan_id = pId,
                rating = _uiState.value.rating,
                ulasan = _uiState.value.ulasan
            )

            createReviewUseCase(review, photoBytes).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

data class ReviewUiState(
    val rating: Int = 5,
    val ulasan: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

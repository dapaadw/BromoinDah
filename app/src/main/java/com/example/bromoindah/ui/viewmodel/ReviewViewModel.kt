package com.example.bromoindah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.data.model.Review
import com.example.bromoindah.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val submitSuccess: Boolean = false,
    val hasReviewed: Boolean = false
)

class ReviewViewModel : ViewModel() {
    private val repo = ReviewRepository

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun submitReview(
        pesananId: String,
        userId: String,
        wisataId: String,
        rating: Int,
        ulasan: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val review = Review(
                pesananId = pesananId,
                userId = userId,
                wisataId = wisataId,
                rating = rating,
                ulasan = ulasan
            )
            repo.addReview(review).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        submitSuccess = true,
                        hasReviewed = true
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Gagal mengirim ulasan"
                    )
                }
            )
        }
    }

    fun checkHasReviewed(pesananId: String) {
        viewModelScope.launch {
            repo.hasReviewed(pesananId).fold(
                onSuccess = { reviewed ->
                    _uiState.value = _uiState.value.copy(hasReviewed = reviewed)
                },
                onFailure = { /* Non-critical, default to false */ }
            )
        }
    }

    fun resetSubmitSuccess() {
        _uiState.value = _uiState.value.copy(submitSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

package com.example.bromoindah.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.model.User
import com.example.bromoindah.domain.repository.AuthRepository
import com.example.bromoindah.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _uiState.update { it.copy(user = user) }
                if (user != null) {
                    bookingRepository.getBookingsByUserId(user.id).collect { bookings ->
                        _uiState.update { it.copy(bookingCount = bookings.size) }
                    }
                }
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            authRepository.signOut().onSuccess {
                _uiState.update { it.copy(isLoggedOut = true) }
            }
        }
    }
}

data class ProfileUiState(
    val user: User? = null,
    val bookingCount: Int = 0,
    val isLoggedOut: Boolean = false
)

package com.example.bromoindah.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.data.model.Profile
import com.example.bromoindah.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null,
    val isAdmin: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun checkSession() {
        viewModelScope.launch {
            val loggedIn = repo.isLoggedIn()
            if (loggedIn) {
                loadProfile()
            } else {
                _uiState.value = AuthUiState(isLoggedIn = false)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.signIn(email, password).fold(
                onSuccess = { loadProfile() },
                onFailure = { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Login gagal") }
            )
        }
    }

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repo.signUp(email, password, fullName).fold(
                onSuccess = { loadProfile() },
                onFailure = { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Registrasi gagal") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.signOut()
            _uiState.value = AuthUiState(isLoggedIn = false)
        }
    }

    private suspend fun loadProfile() {
        repo.getUserProfile().fold(
            onSuccess = { profile ->
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    profile = profile,
                    isAdmin = profile.role == "admin",
                    isLoading = false
                )
            },
            onFailure = {
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    isLoading = false,
                    error = it.message
                )
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

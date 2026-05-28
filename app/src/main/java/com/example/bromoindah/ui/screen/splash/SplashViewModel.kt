package com.example.bromoindah.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bromoindah.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination = _destination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser().firstOrNull()
            if (user != null) {
                if (user.role == "admin") {
                    _destination.value = SplashDestination.AdminHome
                } else {
                    _destination.value = SplashDestination.UserHome
                }
            } else {
                _destination.value = SplashDestination.Login
            }
        }
    }
}

sealed class SplashDestination {
    object Login : SplashDestination()
    object UserHome : SplashDestination()
    object AdminHome : SplashDestination()
}

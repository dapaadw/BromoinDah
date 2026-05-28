package com.example.bromoindah.domain.usecase.auth

import com.example.bromoindah.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, namaLengkap: String): Result<Unit> {
        if (email.isBlank() || password.isBlank() || namaLengkap.isBlank()) {
            return Result.failure(Exception("All fields must be filled"))
        }
        if (password.length < 8) {
            return Result.failure(Exception("Password must be at least 8 characters"))
        }
        return repository.signUp(email, password, namaLengkap)
    }
}

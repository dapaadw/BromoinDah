package com.example.bromoindah.domain.usecase.auth

import com.example.bromoindah.domain.repository.AuthRepository
import javax.inject.Inject
import javax.xml.transform.Result

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }
        return repository.signIn(email, password)
    }
}

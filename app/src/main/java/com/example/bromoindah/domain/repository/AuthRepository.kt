package com.example.bromoindah.domain.repository

import com.example.bromoindah.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String, namaLengkap: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
    suspend fun getSession(): String?
}

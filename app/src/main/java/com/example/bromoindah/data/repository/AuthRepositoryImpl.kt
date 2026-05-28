package com.example.bromoindah.data.repository

import com.example.bromoindah.domain.model.User
import com.example.bromoindah.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, namaLengkap: String): Result<Unit> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("nama_lengkap", namaLengkap)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        // Gunakan sessionStatus alih-alih sessionFlow untuk reaktivitas status login
        return auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val supabaseUser = status.session.user
                    User(
                        id = supabaseUser?.id ?: "",
                        email = supabaseUser?.email ?: "",
                        // Gunakan content untuk mengambil nilai string murni tanpa tanda kutip JSON
                        nama_lengkap = supabaseUser?.userMetadata?.get("nama_lengkap")?.jsonPrimitive?.content ?: "",
                        role = supabaseUser?.userMetadata?.get("role")?.jsonPrimitive?.content ?: "user"
                    )
                }
                else -> null
            }
        }
    }

    override suspend fun getSession(): String? {
        return auth.currentAccessTokenOrNull()
    }
}

package com.example.bromoindah.data.repository

import android.R.attr.data
import android.provider.ContactsContract
import com.example.bromoindah.domain.model.User
import com.example.bromoindah.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.MDC.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, namaLengkap: String): Result<Unit> {
        return try {
            auth.signUpWith(ContactsContract.CommonDataKinds.Email) {
                this.email = email
                this.password = password

                // Gunakan buildJsonObject untuk mengirim metadata
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
            auth.signInWith(ContactsContract.CommonDataKinds.Email) {
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
        // Gunakan sessionStatus alih-alih sessionFlow
        return auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    // Mengambil objek UserInfo bawaan Supabase
                    val supabaseUser = status.session.user

                    // Mapping ke model domain 'User' milik Anda
                    User(
                        id = supabaseUser?.id ?: "",
                        email = supabaseUser?.email ?: "",
                        // Gunakan jsonPrimitive.content agar tidak ada "tanda kutip" berlebih
                        nama_lengkap = supabaseUser?.userMetadata?.get("nama_lengkap")?.jsonPrimitive?.content ?: "",
                        role = supabaseUser?.userMetadata?.get("role")?.jsonPrimitive?.content ?: "user"
                    )
                }
                else -> null // Return null jika statusnya NotAuthenticated, NetworkError, atau Loading
            }
        }
    }

    override suspend fun getSession(): String? {
        return auth.currentAccessTokenOrNull()
    }
}

package com.example.bromoindah.data.repository

import com.example.bromoindah.data.SupabaseClient
import com.example.bromoindah.data.model.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

object AuthRepository {
    private val client = SupabaseClient.client

    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            // Insert profile row after successful sign up
            val userId = client.auth.currentUserOrNull()?.id ?: throw Exception("User ID not found after sign up")
            val profile = Profile(
                id = userId,
                fullName = fullName,
                role = "user"
            )
            client.postgrest.from("profiles").insert(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    suspend fun getUserProfile(): Result<Profile> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val profile = client.postgrest.from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<Profile>()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }
}

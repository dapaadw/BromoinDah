package com.example.bromoindah.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class User(
    val id: String,
    val nama_lengkap: String,
    val email: String,
    val foto_profil_url: String? = null,
    val role: String = "user",
    val created_at: Instant? = null
)
package com.example.bromoindah.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    @SerialName("user")
    USER,
    @SerialName("admin")
    ADMIN
}

@Serializable
data class Profile(
    val id: String = "",
    @SerialName("full_name") val fullName: String = "",
    val role: UserRole = UserRole.USER,
    val phone: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

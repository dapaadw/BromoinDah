package com.example.bromoindah.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String = "",
    @SerialName("full_name") val fullName: String = "",
    val role: String = "user",
    val phone: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

package com.example.bromoindah.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class Review(
    val id: String? = null,
    val user_id: String,
    val wisata_id: String,
    val pesanan_id: String,
    val rating: Int,
    val ulasan: String? = null,
    val foto_review_url: String? = null,
    val created_at: Instant? = null
)

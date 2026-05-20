package com.example.bromoindah.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Review(
    @EncodeDefault(EncodeDefault.Mode.NEVER) val id: String? = null,
    @SerialName("pesanan_id") val pesananId: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("wisata_id") val wisataId: String = "",
    val rating: Int = 5,
    val ulasan: String = "",
    @EncodeDefault(EncodeDefault.Mode.NEVER) @SerialName("created_at") val createdAt: String? = null,
    // Joined
    val profiles: Profile? = null
)

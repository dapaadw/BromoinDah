package com.example.bromoindah.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Wisata(
    @EncodeDefault(EncodeDefault.Mode.NEVER) val id: String? = null,
    val nama: String = "",
    val lokasi: String = "",
    val deskripsi: String = "",
    @SerialName("harga_tiket") val hargaTiket: Int = 0,
    @SerialName("jam_operasional") val jamOperasional: String = "",
    val fasilitas: String = "",
    val aturan: String = "",
    @SerialName("image_url") val imageUrl: String = "",
    val gallery: List<String> = emptyList(),
    @EncodeDefault(EncodeDefault.Mode.NEVER) @SerialName("created_at") val createdAt: String? = null
)

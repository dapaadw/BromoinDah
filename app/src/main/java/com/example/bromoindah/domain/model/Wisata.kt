package com.example.bromoindah.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Wisata(
    val id: String? = null,
    val nama_wisata: String,
    val deskripsi: String,
    val lokasi: String,
    val harga_tiket: Int,
    val jam_operasional: String? = null,
    val fasilitas: String? = null,
    val aturan_kunjungan: String? = null,
    val foto_wisata_url: String? = null,
    val created_at: String? = null
)

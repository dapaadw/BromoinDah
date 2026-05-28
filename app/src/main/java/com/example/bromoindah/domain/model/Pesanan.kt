package com.example.bromoindah.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pesanan(
    val id: String? = null,
    val user_id: String,
    val wisata_id: String,
    val tanggal_pesan: String,
    val jumlah_tiket: Int,
    val total_harga: Int,
    val bukti_bayar_url: String? = null,
    val status_pesanan: String = "Menunggu Pembayaran",
    val created_at: String? = null
)

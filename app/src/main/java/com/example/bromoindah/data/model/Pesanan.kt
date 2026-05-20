package com.example.bromoindah.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Pesanan(
    @EncodeDefault(EncodeDefault.Mode.NEVER) val id: String? = null,
    @SerialName("user_id") val userId: String = "",
    @SerialName("wisata_id") val wisataId: String = "",
    @SerialName("tanggal_kunjungan") val tanggalKunjungan: String = "",
    @SerialName("jumlah_tiket") val jumlahTiket: Int = 1,
    @SerialName("total_harga") val totalHarga: Int = 0,
    val status: String = "pending",
    @SerialName("bukti_bayar_url") val buktiBayarUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) @SerialName("created_at") val createdAt: String? = null,
    // Joined data (not always present)
    val wisata: Wisata? = null
)

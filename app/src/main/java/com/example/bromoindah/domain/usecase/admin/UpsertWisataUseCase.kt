package com.example.bromoindah.domain.usecase.admin

import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import javax.inject.Inject

class UpsertWisataUseCase @Inject constructor(
    private val repository: WisataRepository
) {
    suspend operator fun invoke(wisata: Wisata): Result<Unit> {
        if (wisata.nama_wisata.isBlank() || wisata.deskripsi.isBlank() || wisata.lokasi.isBlank()) {
            return Result.failure(Exception("Nama, deskripsi, dan lokasi tidak boleh kosong"))
        }
        return repository.upsertWisata(wisata)
    }
}

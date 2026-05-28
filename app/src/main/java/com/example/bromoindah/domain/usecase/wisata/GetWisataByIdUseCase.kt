package com.example.bromoindah.domain.usecase.wisata

import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import javax.inject.Inject

class GetWisataByIdUseCase @Inject constructor(
    private val repository: WisataRepository
) {
    suspend operator fun invoke(id: String): Wisata? = repository.getWisataById(id)
}

package com.example.bromoindah.domain.usecase.wisata

import com.example.bromoindah.domain.model.Wisata
import com.example.bromoindah.domain.repository.WisataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWisataListUseCase @Inject constructor(
    private val repository: WisataRepository
) {
    operator fun invoke(): Flow<List<Wisata>> = repository.getWisataList()
}

package com.example.bromoindah.domain.repository

import com.example.bromoindah.domain.model.Wisata
import kotlinx.coroutines.flow.Flow

interface WisataRepository {
    fun getWisataList(): Flow<List<Wisata>>
    suspend fun getWisataById(id: String): Wisata?
    suspend fun searchWisata(query: String): List<Wisata>
    suspend fun upsertWisata(wisata: Wisata): Result<Unit>
    suspend fun deleteWisata(id: String): Result<Unit>
}

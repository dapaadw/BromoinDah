package com.example.bromoindah.domain.usecase.admin

import com.example.bromoindah.domain.repository.WisataRepository
import javax.inject.Inject

class UploadWisataImageUseCase @Inject constructor(
    private val repository: WisataRepository
) {
    suspend operator fun invoke(byteArray: ByteArray, fileName: String): Result<String> {
        return repository.uploadWisataImage(byteArray, fileName)
    }
}

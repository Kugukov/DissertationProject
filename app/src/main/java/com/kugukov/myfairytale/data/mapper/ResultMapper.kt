package com.kugukov.myfairytale.data.mapper

import com.kugukov.myfairytale.data.network.UpdateResultDto
import com.kugukov.myfairytale.data.network.UploadResultDto
import com.kugukov.myfairytale.domain.model.UpdateResult
import com.kugukov.myfairytale.domain.model.UploadResult

fun UpdateResultDto.toDomain(): UpdateResult {
    return UpdateResult(
        isDangerous = dangerous,
        dangerWords = dangerWords,
        taleId = taleId
    )
}

fun UploadResultDto.toDomain(): UploadResult {
    return UploadResult(
        isDangerous = dangerous,
        dangerWords = dangerWords,
        taleId = taleId
    )
}

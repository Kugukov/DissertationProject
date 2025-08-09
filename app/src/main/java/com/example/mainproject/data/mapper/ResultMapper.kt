package com.example.mainproject.data.mapper

import com.example.mainproject.data.network.UpdateResultDto
import com.example.mainproject.data.network.UploadResultDto
import com.example.mainproject.domain.model.UpdateResult
import com.example.mainproject.domain.model.UploadResult

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

package com.kugukov.myfairytale.domain.model

data class UploadResult(
    val isDangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
)

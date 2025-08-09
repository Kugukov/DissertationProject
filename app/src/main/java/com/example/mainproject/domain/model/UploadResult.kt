package com.example.mainproject.domain.model

data class UploadResult(
    val isDangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
)

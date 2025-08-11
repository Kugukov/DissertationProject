package com.kugukov.myfairytale.data.network

data class UploadResultDto(
    val dangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
)

package com.kugukov.myfairytale.domain.model

data class UpdateResult(
    val isDangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
)

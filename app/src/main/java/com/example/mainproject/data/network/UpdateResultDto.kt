package com.example.mainproject.data.network

data class UpdateResultDto(
    val dangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
)
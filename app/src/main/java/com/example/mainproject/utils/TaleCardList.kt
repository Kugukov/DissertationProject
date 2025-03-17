package com.example.mainproject.utils

import androidx.compose.runtime.MutableState

data class TaleCardList(
    val taleId: Int,
    val title: MutableState<String>,
    val description: MutableState<String>
)

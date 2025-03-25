package com.example.mainproject.ui.components.textTales

import androidx.compose.runtime.MutableState

data class TextTale(
    val textTaleId: Int,
    val title: MutableState<String>,
    val description: MutableState<String>
)

package com.example.mainproject.ui.components.audioTales

import androidx.compose.runtime.MutableState
import java.io.File

data class AudioTale(
    val audioTaleId: Int,
    val title: MutableState<String>,
    val description: MutableState<String>,
    val audioFile: File
    /*TODO длительность записи и время создания*/
)

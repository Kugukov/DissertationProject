package com.example.mainproject.ui.components.audioTales

import java.io.File

data class AudioTale(
    val title: String,
    val description: String,
    val audioFile: File,
    val audioDuration: String
)

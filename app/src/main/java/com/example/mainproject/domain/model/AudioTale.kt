package com.example.mainproject.domain.model

import java.io.File

data class AudioTale(
    val title: String,
    val description: String,
    val audioFile: File,
    val audioDuration: String
)

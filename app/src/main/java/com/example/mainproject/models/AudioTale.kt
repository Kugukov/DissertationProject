package com.example.mainproject.models

import java.io.File

data class AudioTale(
    val title: String,
    val description: String,
    val audioFile: File,
    val audioDuration: String
)

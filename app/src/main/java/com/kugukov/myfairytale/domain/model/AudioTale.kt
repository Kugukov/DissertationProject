package com.kugukov.myfairytale.domain.model

import java.io.File

data class AudioTale(
    val title: String,
    val description: String,
    val audioFile: File,
    val audioDuration: String
)

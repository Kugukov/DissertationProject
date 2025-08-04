package com.example.mainproject.domain

import java.io.File

interface AudioFileStorage {
    suspend fun downloadAndSaveAudio(fileName: String): Result<File>
    suspend fun getOrDownloadAudioFile(fileUrl: String): Result<File>
}
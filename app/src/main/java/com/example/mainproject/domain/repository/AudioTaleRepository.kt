package com.example.mainproject.domain.repository

import com.example.mainproject.domain.model.UpdateResult
import com.example.mainproject.domain.model.UploadResult
import com.example.mainproject.domain.model.AudioTale
import com.example.mainproject.domain.model.AudioTaleDB
import java.io.File

interface AudioTaleRepository {
    suspend fun fetchAudioTales(deviceId: String): Result<List<AudioTaleDB>>
    suspend fun uploadAudioTale(tale: AudioTale, deviceId: String): Result<UploadResult>
    suspend fun updateAudioTale(
        taleId: Int,
        newTitle: String,
        newDescription: String,
        newAudioFile: File?,
        newAudioDuration: String?
    ): Result<UpdateResult>
    suspend fun deleteAudioTale(taleId: Int): Result<Unit>
}
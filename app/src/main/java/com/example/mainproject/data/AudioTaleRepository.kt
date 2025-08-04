package com.example.mainproject.data

import com.example.mainproject.domain.UpdateResult
import com.example.mainproject.domain.UploadResult
import com.example.mainproject.models.AudioTale
import com.example.mainproject.models.AudioTaleDB
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
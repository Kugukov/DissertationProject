package com.kugukov.myfairytale.domain.repository

import com.kugukov.myfairytale.domain.model.UpdateResult
import com.kugukov.myfairytale.domain.model.UploadResult
import com.kugukov.myfairytale.domain.model.AudioTale
import com.kugukov.myfairytale.domain.model.AudioTaleDB
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
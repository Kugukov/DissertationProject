package com.example.mainproject.data

import com.example.mainproject.domain.UpdateResult
import com.example.mainproject.domain.UploadResult
import com.example.mainproject.network.ApiService
import com.example.mainproject.models.AudioTale
import com.example.mainproject.models.AudioTaleDB
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AudioTaleRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AudioTaleRepository {

    override suspend fun fetchAudioTales(deviceId: String): Result<List<AudioTaleDB>> {
        return try {
            val response = apiService.getAudioTales(deviceId)
            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(Exception("Ошибка API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAudioTale(
        tale: AudioTale,
        deviceId: String
    ): Result<UploadResult> {
        return try {
            val apiService = this.apiService

            val metadataJson = Gson().toJson(
                tale.copy(audioFile = File("")) // файл отдельно, здесь только мета-данные
            )
            val metadataBody = metadataJson.toRequestBody("application/json".toMediaTypeOrNull())

            val deviceIdBody = deviceId.toRequestBody("text/plain".toMediaTypeOrNull())

            val fileRequestBody =
                tale.audioFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            val filePart = MultipartBody.Part.createFormData(
                name = "file",
                filename = tale.audioFile.name,
                body = fileRequestBody
            )

            val response = apiService.uploadAudioTale(
                deviceId = deviceIdBody,
                metadata = metadataBody,
                file = filePart
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val result = UploadResult.from(body)
                    Result.success(result)
                } else {
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                Result.failure(Exception("Ошибка от API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAudioTale(
        taleId: Int,
        newTitle: String,
        newDescription: String,
        newAudioFile: File?,
        newAudioDuration: String?
    ): Result<UpdateResult> {
        return try {
            val titleBody = newTitle.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = newDescription.toRequestBody("text/plain".toMediaTypeOrNull())
            val durationBody = newAudioDuration?.toRequestBody("text/plain".toMediaTypeOrNull())

            val filePart = newAudioFile?.let {
                val requestFile = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", it.name, requestFile)
            }

            val response = apiService.updateAudioTale(
                taleId = taleId,
                title = titleBody,
                description = descriptionBody,
                audioDuration = durationBody,
                file = filePart
            )

            if (response.isSuccessful) {
                val map = response.body()
                if (map != null) {
                    val result = UpdateResult.from(map)
                    Result.success(result)
                } else {
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                Result.failure(Exception("Ошибка ${response.code()}: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAudioTale(taleId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteAudioTale(taleId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка удаления: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
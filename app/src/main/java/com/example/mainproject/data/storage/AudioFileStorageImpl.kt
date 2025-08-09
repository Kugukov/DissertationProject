package com.example.mainproject.data.storage

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.mainproject.data.network.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioFileStorageImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : AudioFileStorage {
    override suspend fun downloadAndSaveAudio(fileName: String): Result<File> {
        return try {
            val response = apiService.downloadFile(fileName)
            if (!response.isSuccessful) {
                return Result.failure(Exception("Download error: ${response.code()}"))
            }

            val responseBody =
                response.body() ?: return Result.failure(Exception("Empty answer body"))

            val dir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            val file = File(dir, fileName)

            file.outputStream().use { outputStream ->
                responseBody.byteStream().copyTo(outputStream)
            }

            Result.success(file)

        } catch (e: Exception) {
            Log.e("Download", "Error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getOrDownloadAudioFile(fileUrl: String): Result<File> {
        val fileName = fileUrl.substringAfterLast("\\")
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        if (!file.exists()) {
            val result = downloadAndSaveAudio(fileName)
            if (result.isFailure) return result
        }

        return Result.success(file)
    }

}
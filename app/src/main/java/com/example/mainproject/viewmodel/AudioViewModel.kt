package com.example.mainproject.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.ui.components.audioTales.AudioTale
import com.example.mainproject.ui.components.audioTales.AudioTaleDB
import com.example.mainproject.utils.ApiConfig
import com.example.mainproject.utils.DeviceInfo
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AudioViewModel : ViewModel() {
    private val apiService = ApiConfig.createApiService()

    private val _fetchSuccess = MutableStateFlow(false)
    val fetchSuccess: StateFlow<Boolean> = _fetchSuccess

    private val _isDangerous = MutableLiveData<Boolean?>().apply { value = null }
    val isDangerous: LiveData<Boolean?> get() = _isDangerous
    private val _dangerousWords = MutableLiveData<List<String>?>().apply { value = null }
    val dangerousWords: LiveData<List<String>?> get() = _dangerousWords

    private val _audioTalesDB = MutableStateFlow<List<AudioTaleDB>>(emptyList())
    val audioTalesDB: StateFlow<List<AudioTaleDB>> = _audioTalesDB.asStateFlow()

    fun updateDangerStatus(isDangerous: Boolean?, dangerousWords: List<String>?) {
        _isDangerous.value = isDangerous
        _dangerousWords.value = dangerousWords
    }

    fun fetchAudioTales(context: Context) {
        viewModelScope.launch {
            try {
                val getDeviceId = DeviceInfo.getDeviceInfo(context)["device_id"]
                val audioFiles = apiService.getAudioTales(getDeviceId!!)
                if (audioFiles.isSuccessful) {
                    _fetchSuccess.value = true
                    _audioTalesDB.value = audioFiles.body() ?: emptyList()
                    Log.d("fetch Audio Card: ", "${audioFiles.body()}")
                }

            } catch (e: Exception) {
                Log.e("Error", "Ошибка получения файлов: ${e.message}")
                _fetchSuccess.value = false
            }
        }
    }

    fun uploadAudioData(context: Context, audioTale: AudioTale) {
        val service = ApiConfig.createApiService()

        val metadataJson = Gson().toJson(
            audioTale.copy(
                audioFile = File(""),
                title = audioTale.title,
                description = audioTale.description,
                audioDuration = audioTale.audioDuration,
            )
        )
        val metadataBody = metadataJson.toRequestBody("application/json".toMediaTypeOrNull())

        val deviceIdBody = DeviceInfo.getDeviceInfo(context)["device_id"]!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val fileRequestBody =
            audioTale.audioFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("file", audioTale.audioFile.name, fileRequestBody)

        viewModelScope.launch {
            try {
                val response = service.uploadAudioTale(deviceIdBody, metadataBody, filePart)
                if (response.isSuccessful) {
                    val isDangerous = response.body()?.get("dangerous") as Boolean
                    if (isDangerous) {
                        val dangerWords = response.body()?.get("danger_words") as? List<String>?
                        updateDangerStatus(true, dangerWords)
                        val taleId = response.body()?.get("tale_id") as? Int
                        Log.d("Upload AudioTale", "tale_id: ${response.body()?.get("tale_id")}")
                        Log.d("Upload AudioTale", "Upload is Dangerous. Danger words: $dangerWords")
                        deleteAudioTale(context, taleId!!)
                    } else {
                        updateDangerStatus(false, null)
                        Log.e("Upload AudioTale", "Upload is Safe")
                    }
                    fetchAudioTales(context)
                    Log.d("Upload AudioTale", "Файл успешно отправлен, ответ сервера: $response")
                }

            } catch (e: Exception) {
                Log.e("Upload AudioTale", "Ошибка: ${e.message}")
            }
        }
    }

    suspend fun downloadAndSaveAudio(context: Context, fileName: String): File? {
        val apiService = ApiConfig.createApiService()

        return try {
            val response = apiService.downloadFile(fileName)

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return null

                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
                file.outputStream().use { outputStream ->
                    responseBody.byteStream().copyTo(outputStream)
                }

                return file
            } else {
                return null
            }

        } catch (e: Exception) {
            Log.e("Download", "Ошибка: ${e.message}")
            null
        }
    }

    fun deleteAudioTale(context: Context, taleId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteAudioTale(taleId)
                if (response.isSuccessful) {
                    Log.d("Delete", "Файл успешно удален")
                }
                fetchAudioTales(context)
            } catch (e: Exception) {
                Log.e("AudioTaleViewModel", "Ошибка удаления: ${e.message}")
            }
        }
    }

    fun updateAudioTale(
        context: Context,
        taleId: Int,
        newTitle: String,
        newDescription: String,
        newAudioFile: File?, // Если перезаписан файл, иначе null
        newAudioDuration: String?
    ) {
        val apiService = ApiConfig.createApiService()

        val titleBody = newTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = newDescription.toRequestBody("text/plain".toMediaTypeOrNull())
        val durationBody = newAudioDuration?.toRequestBody("text/plain".toMediaTypeOrNull())

        // Если новый файл существует, подготавливаем его для multipart запроса
        val filePart = newAudioFile?.let {
            val requestFile = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", it.name, requestFile)
        }

        viewModelScope.launch {
            try {
                Log.d("Update AudioTale response", " $taleId, $newTitle, $newDescription, $newAudioDuration, $newAudioFile")
                val response = apiService.updateAudioTale(
                    taleId,
                    titleBody,
                    descriptionBody,
                    durationBody,
                    filePart
                )
                if (response.isSuccessful) {
                    val isDangerous = response.body()?.get("dangerous") as Boolean
                    if (isDangerous) {
                        val dangerWords = response.body()?.get("danger_words") as? List<String>?
                        updateDangerStatus(true, dangerWords)
                        Log.d("Update AudioTale", "Upload is Dangerous. Danger words: $dangerWords")
                        deleteAudioTale(context, taleId)
                    } else {
                        updateDangerStatus(false, null)
                        Log.d("Update AudioTale", "Сказка успешно обновлена")
                    }
                    fetchAudioTales(context)
                    Log.d("Update AudioTale", "Файл успешно отправлен, ответ сервера: $response")
                } else {
                    Log.e("Update AudioTale", "Ошибка обновления: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Update AudioTale", "Ошибка: ${e.message}")
            }
        }
    }


    @SuppressLint("DefaultLocale")
    fun getAudioDuration(file: File): String {
        val mediaPlayer = MediaPlayer()
        return try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            val ms = mediaPlayer.duration.toLong()
            val minutes = (ms / 1000) / 600
            val secs = (ms / 1000) % 600
            return String.format("%02d:%02d", minutes, secs)
        } catch (e: Exception) {
            ""
        } finally {
            mediaPlayer.release()
        }
    }
}
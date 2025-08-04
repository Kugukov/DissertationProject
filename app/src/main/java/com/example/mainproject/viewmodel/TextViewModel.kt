package com.example.mainproject.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.models.TextTale
import com.example.mainproject.utils.ApiConfig
import com.example.mainproject.utils.DeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val apiService = ApiConfig.createApiService()

    private val _textTalesList = MutableStateFlow<List<TextTale>>(emptyList())
    val textTalesList: StateFlow<List<TextTale>> = _textTalesList.asStateFlow()

    fun fetchTextTales() {
        viewModelScope.launch {
            try {
                val getDeviceId = DeviceInfo.getDeviceInfo(context)["device_id"]
                val textFiles = apiService.getTextTales(getDeviceId!!)

                if (textFiles.isSuccessful) {
                    Log.d("fetch Text Card: ", "${textFiles.body()}")
                    _textTalesList.value = textFiles.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("Error", "Ошибка получения файлов: ${e.message}")
            }
        }
    }

    fun uploadTextTale(textTale: TextTale) {
        val apiService = ApiConfig.createApiService()

        val deviceIdBody = DeviceInfo.getDeviceInfo(context)["device_id"]!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val titleBody = textTale.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = textTale.description.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = apiService.uploadTextTale(deviceIdBody, titleBody, descriptionBody)
                if (response.isSuccessful) {
                    fetchTextTales()
                    Log.d("Upload TextTale", "Сказка успешно загружена")
                } else {
                    Log.e("Upload TextTale", "Ошибка загрузки: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Upload TextTale", "Ошибка: ${e.message}")
            }
        }
    }

    fun deleteTextTale(taleId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteTextTale(taleId)
                if (response.isSuccessful) {
                    Log.d("Delete", "Файл успешно удален")
                }
                fetchTextTales()
            } catch (e: Exception) {
                Log.e("AudioTaleViewModel", "Ошибка удаления: ${e.message}")
            }
        }
    }

    fun updateTextTale(taleId: Int, newTitle: String, newDescription: String) {
        val apiService = ApiConfig.createApiService()

        val titleBody = newTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = newDescription.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = apiService.updateTextTale(
                    taleId,
                    titleBody,
                    descriptionBody
                )
                if (response.isSuccessful) {
                    fetchTextTales()
                    Log.d("Update", "TextTale успешно обновлена")
                } else {
                    Log.e("Update", "TextTale Ошибка обновления: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Update", "TextTale Ошибка: ${e.message}")
            }
        }
    }
}
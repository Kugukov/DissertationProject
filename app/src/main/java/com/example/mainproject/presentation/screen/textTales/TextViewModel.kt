package com.example.mainproject.presentation.screen.textTales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.core.config.ApiConfig
import com.example.mainproject.domain.DeviceInfoProvider
import com.example.mainproject.domain.model.TextTale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel() {
    private val apiService = ApiConfig.createApiService()

    private val _textTalesList = MutableStateFlow<List<TextTale>>(emptyList())
    val textTalesList: StateFlow<List<TextTale>> = _textTalesList.asStateFlow()

    fun fetchTextTales() {
        viewModelScope.launch {
            try {
                val getDeviceId = deviceInfoProvider.getDeviceInfo()["device_id"] ?: "unknown"
                val textFiles = apiService.getTextTales(getDeviceId)

                if (textFiles.isSuccessful) {
                    Log.d("fetch Text Card: ", "${textFiles.body()}")
                    _textTalesList.value = textFiles.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("Error", "Fetch text tales error: ${e.message}")
            }
        }
    }

    fun uploadTextTale(textTale: TextTale) {
        val apiService = ApiConfig.createApiService()

        val deviceIdBody = deviceInfoProvider.getDeviceInfo()["device_id"]!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val titleBody = textTale.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = textTale.description.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = apiService.uploadTextTale(deviceIdBody, titleBody, descriptionBody)
                if (response.isSuccessful) {
                    fetchTextTales()
                    Log.d("Upload TextTale", "Upload successful")
                } else {
                    Log.e("Upload TextTale", "Upload error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Upload TextTale", "Error: ${e.message}")
            }
        }
    }

    fun deleteTextTale(taleId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteTextTale(taleId)
                if (response.isSuccessful) {
                    Log.d("Delete TextTale", "Delete successful")
                }
                fetchTextTales()
            } catch (e: Exception) {
                Log.e("Delete TextTale", "Error: ${e.message}")
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
                    Log.d("Update TextTale", "Update successful")
                } else {
                    Log.e("Update TextTale", "Update error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Update TextTale", "Error: ${e.message}")
            }
        }
    }
}
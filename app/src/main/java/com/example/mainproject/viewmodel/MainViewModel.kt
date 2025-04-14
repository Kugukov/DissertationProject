package com.example.mainproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.models.SharedPreferencesManager
import com.example.mainproject.network.ApiService
import com.example.mainproject.ui.components.textTales.TextTale
import com.example.mainproject.utils.ApiConfig
import com.example.mainproject.utils.DeviceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MainViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val apiService = ApiConfig.createApiService()

    private val _checkDeviceDataSuccess = MutableStateFlow(false)
    val checkDeviceDataSuccess: StateFlow<Boolean> = _checkDeviceDataSuccess

    /* Первый запуск */
    private var _isFirstLaunch = mutableStateOf(true)
    val isFirstLaunch: State<Boolean> = _isFirstLaunch

    /* Пароль */
    private val _password = MutableStateFlow("123") // Установленный
    private val _passwordValue = mutableStateOf("") // Вводимый
    val password = _password.asStateFlow()
    val passwordValue: State<String> = _passwordValue

    /* Родитель? */
    private val _isParent = mutableStateOf(true)
    val isParent: State<Boolean> = _isParent

    /* Список текстовых сказок */
    private val _textTalesList = MutableStateFlow<List<TextTale>>(emptyList())
    val textTalesList: StateFlow<List<TextTale>> = _textTalesList.asStateFlow()

    /* Настройка картинок */
    var childImage: Bitmap? = null
        private set
    var parentImage: Bitmap? = null
        private set

    init {
        _password.value = sharedPreferencesManager.getPassword()
        _isFirstLaunch.value = sharedPreferencesManager.getFirstLaunch()
//        loadAudioFiles()
    }

    // Получение текстовых сказок с сервера
    fun fetchTextTales(context: Context) {
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

    fun checkDeviceData(context: Context, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val deviceInfo = DeviceInfo.getDeviceInfo(context)["device_id"]
                val response: Response<ApiService.DeviceResponse> = apiService.checkRegisterDevice(deviceInfo!!)
                if (response.isSuccessful) {
                    val exists = response.body()?.exists ?: false
                    _checkDeviceDataSuccess.value = exists
                    callback(exists)
                    Log.d("register", "register done: $exists")
                } else {
                    Log.e("register", "Ошибка загрузки: ${response.errorBody()?.string()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("register", "Ошибка: ${e.message}")
                callback(false)
            }
        }
    }

    fun registerDeviceData(context: Context) {
        val apiService = ApiConfig.createApiService()

        viewModelScope.launch {
            try {
                val deviceInfo = DeviceInfo.getDeviceInfo(context)
                val response = apiService.registerDevice(deviceInfo)
                if (response.isSuccessful) {
                    _checkDeviceDataSuccess.value = true
                    Log.d("register", "register done")
                } else {
                    Log.e("register", "Ошибка загрузки: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("register", "Ошибка: ${e.message}")
            }
        }
    }

    fun uploadTextData(context: Context, textTale: TextTale) {
        val apiService = ApiConfig.createApiService()

        val deviceIdBody = DeviceInfo.getDeviceInfo(context)["device_id"]!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val titleBody = textTale.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = textTale.description.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = apiService.uploadTextTale(deviceIdBody, titleBody, descriptionBody)
                if (response.isSuccessful) {
                    fetchTextTales(context)
                    Log.d("Upload TextTale", "Сказка успешно загружена")
                } else {
                    Log.e("Upload TextTale", "Ошибка загрузки: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Upload TextTale", "Ошибка: ${e.message}")
            }
        }
    }

    fun deleteTextTale(context: Context, taleId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteTextTale(taleId)
                if (response.isSuccessful) {
                    fetchTextTales(context)
                    Log.d("Delete", "Файл успешно удален")
                }
                fetchTextTales(context)
            } catch (e: Exception) {
                Log.e("AudioTaleViewModel", "Ошибка удаления: ${e.message}")
            }
        }
    }

    fun updateTextTale(
        taleId: Int,
        newTitle: String,
        newDescription: String
    ) {
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
                    Log.d("Update", "TextTale успешно обновлена")
                } else {
                    Log.e("Update", "TextTale Ошибка обновления: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Update", "TextTale Ошибка: ${e.message}")
            }
        }
    }

    /* Обновление флага первого запуска */
    fun updateFirstLaunch() {
        _isFirstLaunch.value = false
        viewModelScope.launch {
            sharedPreferencesManager.disableFirstLaunch()
        }
    }

    /* Вводимый пароль при входе */
    fun updatePasswordValue(newPasswordValue: String) {
        _passwordValue.value = newPasswordValue
    }

    /* Установка пароля */
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        viewModelScope.launch {
            sharedPreferencesManager.savePassword(newPassword)
        }
    }

    /* Определение кто вошел */
    fun updateIsParent(newBool: Boolean) {
        _isParent.value = newBool
    }

    /* Выбор картинки */
    fun loadImage(context: Context, fileName: String) {
        val bitmap = loadImageFromInternalStorage(context, fileName)
        if (fileName == "saved_child_image.jpg") {
            childImage = bitmap
        }
        if (fileName == "saved_parent_image.jpg") {
            parentImage = bitmap
        }

    }

    /* Сохранение картинки */
    fun saveImage(context: Context, uri: Uri, fileName: String) {
        saveImageToInternalStorage(context, uri, fileName)
        loadImage(context, fileName)
    }

    /* Сохранение картинки во внутреннее хранилище */
    private fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /* Загрузка картинки из внутреннего хранилища */
    private fun loadImageFromInternalStorage(context: Context, fileName: String): Bitmap? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
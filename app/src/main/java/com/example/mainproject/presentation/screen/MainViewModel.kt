package com.example.mainproject.presentation.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.core.config.ApiConfig
import com.example.mainproject.data.network.ApiService
import com.example.mainproject.data.storage.SharedPreferencesManager
import com.example.mainproject.domain.DeviceInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

enum class UserRole {
    PARENT,
    CHILD
}

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val deviceInfoProvider: DeviceInfoProvider,
) : ViewModel() {

    private val apiService = ApiConfig.createApiService()

    private val _checkDeviceDataSuccess = MutableStateFlow(false)
    val checkDeviceDataSuccess: StateFlow<Boolean> = _checkDeviceDataSuccess

    private val _savedPassword = MutableStateFlow("123")
    private val _enteredPassword = mutableStateOf("")
    val savedPassword = _savedPassword.asStateFlow()
    val enteredPassword: State<String> = _enteredPassword

    private val _userRole = MutableStateFlow(UserRole.CHILD)
    val userRole: StateFlow<UserRole> = _userRole

    private val _savedChildImage = mutableStateOf<Bitmap?>(null)
    val savedChildImage: State<Bitmap?> = _savedChildImage
    private val _savedParentImage = mutableStateOf<Bitmap?>(null)
    val savedParentImage: State<Bitmap?> = _savedParentImage

    private val _tempChildUriImage = mutableStateOf<Uri?>(null)
    val tempChildUriImage: State<Uri?> = _tempChildUriImage
    private val _tempParentUriImage = mutableStateOf<Uri?>(null)
    val tempParentUriImage: State<Uri?> = _tempParentUriImage

    private val _showExitAccountAlert = MutableStateFlow(false)
    val showExitAccountAlert: StateFlow<Boolean> = _showExitAccountAlert

    private val _showSaveTaleAlert = MutableStateFlow(false)
    val showSaveTaleAlert: StateFlow<Boolean> = _showSaveTaleAlert

    init {
        _savedPassword.value = sharedPreferencesManager.getPassword()
    }

    fun checkDeviceData(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val deviceInfo = deviceInfoProvider.getDeviceInfo()["device_id"]
                val response: Response<ApiService.DeviceResponse> =
                    apiService.checkRegisterDevice(deviceInfo!!)
                if (response.isSuccessful) {
                    val exists = response.body()?.exists ?: false
                    _checkDeviceDataSuccess.value = exists
                    callback(exists)
                    Log.d("Device registration check", "Check done: $exists")
                } else {
                    Log.e("Device registration check", "Check error: ${response.errorBody()?.string()}")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("Device registration check", "Error: ${e.message}")
                callback(false)
            }
        }
    }

    fun registerDeviceData() {
        val apiService = ApiConfig.createApiService()

        viewModelScope.launch {
            try {
                val deviceInfo = deviceInfoProvider.getDeviceInfo()
                val response = apiService.registerDevice(deviceInfo)
                if (response.isSuccessful) {
                    _checkDeviceDataSuccess.value = true
                    Log.i("Device registration", "Registration ${deviceInfo["device_id"]} done")
                } else {
                    Log.e("Device registration", "Registration error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Device registration", "Error: ${e.message}")
            }
        }
    }

    fun updateCurrentEnteredPasswordValue(currentEnteredPassword: String) {
        _enteredPassword.value = currentEnteredPassword
    }

    fun setNewPassword(newPassword: String) {
        _savedPassword.value = newPassword
        viewModelScope.launch {
            sharedPreferencesManager.savePassword(newPassword)
        }
    }

    fun parentEntered() {
        _userRole.value = UserRole.PARENT
    }

    fun childEntered() {
        _userRole.value = UserRole.CHILD
    }

    fun setTempChildUriImage(uri: Uri) {
        _tempChildUriImage.value = uri
    }

    fun setTempParentUriImage(uri: Uri) {
        _tempParentUriImage.value = uri
    }

    fun loadImage(fileName: String) {
        val bitmap = loadImageFromInternalStorage(fileName)
        if (fileName == "saved_child_image.jpg") {
            _savedChildImage.value = bitmap
        }
        if (fileName == "saved_parent_image.jpg") {
            _savedParentImage.value = bitmap
        }
    }

    fun saveImageToInternalStorage(uri: Uri, fileName: String): String? {
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

    private fun loadImageFromInternalStorage(fileName: String): Bitmap? {
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

    fun openExitAccountAlert() {
        _showExitAccountAlert.value = true
    }

    fun closeExitAccountAlert() {
        _showExitAccountAlert.value = false
    }

    fun confirmExitAccountAlert(onNavigateToHome: () -> Unit) {
        run { onNavigateToHome() }
        _showExitAccountAlert.value = false
    }

    fun openSaveTaleAlert() {
        _showSaveTaleAlert.value = true
    }

    fun closeSaveTaleAlert(onNavigateBack: () -> Unit) {
        _showSaveTaleAlert.value = false
        run { onNavigateBack() }
    }
}
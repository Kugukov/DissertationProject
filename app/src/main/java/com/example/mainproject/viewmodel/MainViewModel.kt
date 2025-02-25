package com.example.mainproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.models.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel(private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {

    private val _password = MutableStateFlow("123")
    private val _passwordValue = mutableStateOf("")
    val password = _password.asStateFlow()
    val passwordValue: State<String> = _passwordValue
    var childImage: Bitmap? = null
        private set
    var parentImage: Bitmap? = null
        private set

    init {
        _password.value = sharedPreferencesManager.getPassword()
    }

    fun updatePasswordValue(newPasswordValue: String) {
        _passwordValue.value = newPasswordValue
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        viewModelScope.launch {
            sharedPreferencesManager.savePassword(newPassword)
        }
    }

    fun loadImage(context: Context, fileName: String) {
        val bitmap = loadImageFromInternalStorage(context, fileName)
        if (fileName == "saved_child_image.jpg") {
            childImage = bitmap
        }
        if (fileName == "saved_parent_image.jpg") {
            parentImage = bitmap
        }

    }

    fun saveImage(context: Context, uri: Uri, fileName: String) {
        saveImageToInternalStorage(context, uri, fileName)
        loadImage(context, fileName)
    }

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
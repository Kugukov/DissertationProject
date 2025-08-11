package com.kugukov.myfairytale.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

enum class UserRole { CHILD, PARENT }

@Singleton
class UserProfileRepository @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val PREFS = "app_prefs"
        private const val KEY_PASSWORD = "key_password"
    }

    private val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val _savedPassword = MutableStateFlow(preferences.getString(KEY_PASSWORD, "") ?: "")
    val savedPassword: StateFlow<String> = _savedPassword.asStateFlow()

    private val _currentUserRole = MutableStateFlow<UserRole>(UserRole.CHILD)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    private var _tempChildUriImage = MutableStateFlow<Uri?>(null)
    val tempChildUriImage: StateFlow<Uri?> = _tempChildUriImage.asStateFlow()
    private var _tempParentUriImage = MutableStateFlow<Uri?>(null)
    val tempParentUriImage: StateFlow<Uri?> = _tempParentUriImage.asStateFlow()

    private var _savedChildUriImage = MutableStateFlow<Uri?>(null)
    val savedChildUriImage: StateFlow<Uri?> = _savedChildUriImage.asStateFlow()
    private var _savedParentUriImage = MutableStateFlow<Uri?>(null)
    val savedParentUriImage: StateFlow<Uri?> = _savedParentUriImage.asStateFlow()

    fun setTempChildUriImage(uri: Uri?) {
        _tempChildUriImage.value = uri
    }

    fun setTempParentUriImage(uri: Uri?) {
        _tempParentUriImage.value = uri
    }

    fun saveImageToInternalStorage(uri: Uri, fileName: String) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, fileName)
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun loadImage(fileName: String) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            when (fileName) {
                "saved_child_image.jpg" -> {
                    _savedChildUriImage.value = Uri.fromFile(file)
                }

                "saved_parent_image.jpg" -> {
                    _savedParentUriImage.value = Uri.fromFile(file)
                }
            }
        }
    }

    fun setNewPassword(password: String) {
        preferences.edit { putString(KEY_PASSWORD, password) }
        _savedPassword.value = password
    }

    fun checkPassword(enteredPassword: String): Boolean =
        _savedPassword.value.isNotEmpty() && _savedPassword.value == enteredPassword

    fun setCurrentUserRole(role: UserRole) {
        _currentUserRole.value = role
    }
}
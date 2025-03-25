package com.example.mainproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.models.SharedPreferencesManager
import com.example.mainproject.ui.components.textTales.TextTale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

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
    private val _textTalesList = mutableStateListOf<TextTale>()
    val textTalesList: MutableList<TextTale> = _textTalesList

    /* Заглушка id текстовой сказки */
    private val _newTextTaleId = mutableIntStateOf(0)
    var newTextTaleId: State<Int> = _newTextTaleId

    /* Список аудио сказок */
//    private val _audioTalesList = mutableStateListOf<AudioTaleCardList>()
//    val audioTalesList: MutableList<AudioTaleCardList> get() = _audioTalesList

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

    /* Работа с заглушкой id */
    fun updateNewTaleId() {
        _newTextTaleId.intValue += 1
    }

//    /* Работа с заглушкой id */
//    fun updateNewAudioTaleId() {
//        _newAudioTaleId.intValue += 1
//    }

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

    /* Получение текстовой сказки по id */
    fun getTextTaleById(id: Int): TextTale? {
        return _textTalesList.find { it.textTaleId == id }
    }

    /* Добавление текстовой сказки в список сказок по всем параметрам */
    fun addToTextTalesListByParameter(id: Int, newTaleTitle: String, newTaleDescription: String) {
        _textTalesList.add(
            TextTale(
                id,
                mutableStateOf(newTaleTitle),
                mutableStateOf(newTaleDescription)
            )
        )
    }

    /* Добавление текстовой сказки в список сказок по Tale */
    fun addToTextTalesListByTale(newTale: TextTale) {
        _textTalesList.add(newTale)
    }

    /* Удаление текстовой сказки из списка */
    fun deleteOneOfTextTalesList(id: Int) {
        _textTalesList.removeAll { it.textTaleId == id }
    }

//    private fun loadAudioFiles() {
//        val cacheDir = context.cacheDir
////        val audioDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return
//        val files =
//            cacheDir.listFiles()?.filter { it.extension.lowercase() == "m4a" } ?: emptyList()
//
//        files.forEachIndexed { index, file ->
//            val record = AudioTaleCardList(
//                taleId = index + 1,
//                audioFile = file,
//                title = mutableStateOf(" "),
//                description = mutableStateOf(" "),
//            )
//            _audioTalesList.add(record)
//        }
//    }

//    /* Получение текстовой сказки по id */
//    fun getAudioTaleById(id: Int): TextTaleCardList? {
//        return _textTalesList.find { it.taleId == id }
//    }
//
//    /* Добавление текстовой сказки в список сказок по всем параметрам */
//    fun addToAudioTalesListByParameter(id: Int, newTaleTitle: String, newTaleDescription: String) {
//        _audioTalesList.add(
//            AudioTaleCardList(
//                id,
//                mutableStateOf(newTaleTitle),
//                mutableStateOf(newTaleDescription),
//
//            )
//        )
//    }
//
//    /* Добавление текстовой сказки в список сказок по Tale */
//    fun addToAudioTalesListByTale(newTale: TextTaleCardList) {
//        _textTalesList.add(newTale)
//    }
//
//    /* Удаление текстовой сказки из списка */
//    fun deleteOneOfAudioTalesList(id: Int) {
//        _textTalesList.removeAll { it.taleId == id }
//    }

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
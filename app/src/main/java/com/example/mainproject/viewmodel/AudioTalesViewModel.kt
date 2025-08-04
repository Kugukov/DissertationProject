package com.example.mainproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.data.AudioTaleRepository
import com.example.mainproject.domain.AudioFileStorage
import com.example.mainproject.domain.DeviceInfoProvider
import com.example.mainproject.models.AudioTaleDB
import com.example.mainproject.utils.UiEvent
import com.example.mainproject.utils.workWithAudio.AudioPlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AudioTalesViewModel @Inject constructor(
    private val audioTaleRepository: AudioTaleRepository,
    private val audioFileStorage: AudioFileStorage,
    private val audioPlaybackController: AudioPlaybackController,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel() {

    private val _fetchSuccess = MutableStateFlow(false)
    val fetchSuccess: StateFlow<Boolean> = _fetchSuccess

    private val _audioTalesDB = MutableStateFlow<List<AudioTaleDB>>(emptyList())
    val audioTalesDB: StateFlow<List<AudioTaleDB>> = _audioTalesDB.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    private val _currentlyPlayingCardId = MutableStateFlow<Int?>(null)
    val currentlyPlayingCardId: StateFlow<Int?> = _currentlyPlayingCardId
    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    fun fetchAudioTales() {
        viewModelScope.launch {
            val deviceId = deviceInfoProvider.getDeviceId()
            val result = audioTaleRepository.fetchAudioTales(deviceId = deviceId)

            result.onSuccess { tales ->
                _fetchSuccess.value = true
                _audioTalesDB.value = tales
            }.onFailure {
                Log.e("Error", "Ошибка получения файлов: ${it.message}")
                _fetchSuccess.value = false
            }
        }
    }

    fun deleteAudioTaleByIdInDB(taleId: Int) {
        viewModelScope.launch {
            audioTaleRepository.deleteAudioTale(taleId)
                .onSuccess {
                    Log.d("Delete", "Удалено успешно")
                    fetchAudioTales()
                }
                .onFailure {
                    emitToast("Ошибка при удалении")
                }
        }
    }

    fun checkAudioFileAndPlay(itemId: Int, fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = audioFileStorage.getOrDownloadAudioFile(fileUrl)
            result.onSuccess { file ->
                playPauseAudioTale(itemId, file)
            }.onFailure {
                emitToast("Ошибка загрузки аудио")
            }
        }
    }

    private fun playPauseAudioTale(itemId: Int, audioFile: File) {
        when {
            _currentlyPlayingCardId.value == null || _currentlyPlayingCardId.value != itemId -> {
                audioPlaybackController.stop()
                audioPlaybackController.play(audioFile) {
                    _isPaused.value = true
                }
                _currentlyPlayingCardId.value = itemId
                _isPlaying.value = true
                _isPaused.value = false
            }

            _currentlyPlayingCardId.value == itemId && !_isPaused.value -> {
                audioPlaybackController.pause()
                _isPaused.value = true
            }

            _currentlyPlayingCardId.value == itemId && _isPaused.value -> {
                audioPlaybackController.resume()
                _isPaused.value = false
            }
        }
    }

    fun stopAudioTalePlaying() {
        audioPlaybackController.stop()
        _currentlyPlayingCardId.value = null
        _isPlaying.value = false
        _isPaused.value = false
    }

    private fun emitToast(message: String) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast(message))
        }
    }
}
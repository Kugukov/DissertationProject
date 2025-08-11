package com.kugukov.myfairytale.presentation.screen.audioTales.taleList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.domain.repository.AudioTaleRepository
import com.kugukov.myfairytale.data.storage.AudioFileStorage
import com.kugukov.myfairytale.domain.DeviceInfoProvider
import com.kugukov.myfairytale.domain.model.AudioTaleDB
import com.kugukov.myfairytale.presentation.AudioTaleUiEvent
import com.kugukov.myfairytale.core.utils.workWithAudio.AudioPlaybackController
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

    private val _audioTalesDB = MutableStateFlow<List<AudioTaleDB>>(emptyList())
    val audioTalesDB: StateFlow<List<AudioTaleDB>> = _audioTalesDB.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    private val _currentlyPlayingCardId = MutableStateFlow<Int?>(null)
    val currentlyPlayingCardId: StateFlow<Int?> = _currentlyPlayingCardId
    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private val _eventFlow = MutableSharedFlow<AudioTaleUiEvent>()

    fun fetchAudioTales() {
        viewModelScope.launch {
            val deviceId = deviceInfoProvider.getDeviceInfo()["device_id"] ?: "unknown"
            val result = audioTaleRepository.fetchAudioTales(deviceId = deviceId)

            result.onSuccess { tales ->
                _audioTalesDB.value = tales
            }.onFailure {
                Log.e("Error", "Fetch audio tales failed: ${it.message}")
            }
        }
    }

    fun deleteAudioTaleByIdInDB(taleId: Int) {
        viewModelScope.launch {
            audioTaleRepository.deleteAudioTale(taleId)
                .onSuccess {
                    Log.d("Delete", "Success deleted")
                    fetchAudioTales()
                }
                .onFailure {
                    emitToast("Delete failed: ${it.message}")
                }
        }
    }

    fun checkAudioFileAndPlay(itemId: Int, fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = audioFileStorage.getOrDownloadAudioFile(fileUrl)
            result.onSuccess { file ->
                playPauseAudioTale(itemId, file)
            }.onFailure {
                emitToast("Upload audio failed")
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
            _eventFlow.emit(AudioTaleUiEvent.ShowToast(message))
        }
    }
}
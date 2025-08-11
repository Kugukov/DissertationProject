package com.kugukov.myfairytale.presentation.screen.audioTales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.data.storage.AudioFileStorage
import com.kugukov.myfairytale.core.utils.PermissionsManager
import com.kugukov.myfairytale.presentation.AudioTaleUiEvent
import com.kugukov.myfairytale.core.utils.workWithAudio.AudioPlaybackController
import com.kugukov.myfairytale.core.utils.workWithAudio.WAVAudioRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

abstract class BaseCreateEditAudioTaleViewModel (
    private val audioPlaybackController: AudioPlaybackController,
    private val audioFileStorage: AudioFileStorage,
) : ViewModel() {

    protected val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    protected val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    protected val _audioFile = MutableStateFlow<File?>(null)
    val audioFile: StateFlow<File?> = _audioFile

    protected val _isRecording = MutableStateFlow(false)

    protected val _isPaused = MutableStateFlow(false)

    protected val _isDangerous = MutableStateFlow<Boolean?>(null)
    val isDangerous: StateFlow<Boolean?> = _isDangerous

    protected val _dangerousWords = MutableStateFlow<List<String>>(emptyList())
    val dangerousWords: StateFlow<List<String>> = _dangerousWords

    protected val _showDangerAlert = MutableStateFlow(false)
    val showDangerAlert: StateFlow<Boolean> = _showDangerAlert

    protected val _eventFlow = MutableSharedFlow<AudioTaleUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    protected val _hasMicPermission = MutableStateFlow(false)

    val recordingButtonText: StateFlow<String> = _isRecording.map {
        if (it) "End recording" else "Begin recording"
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Begin recording")

    fun updateTitle(value: String) {
        _title.value = value
    }

    fun updateDescription(value: String) {
        _description.value = value
    }

    fun deleteTempAudio() {
        _audioFile.value?.delete()
        _audioFile.value = null
    }

    fun startStopRecording(recorder: WAVAudioRecorder) {
        if (!_hasMicPermission.value) {
            requestMicPermission()
            return
        }

        if (_isRecording.value) {
            stopRecording(recorder)
        } else {
            deleteTempAudio()
            _isRecording.value = true
            recorder.startRecording()
        }
    }

    fun onMicPermissionResult(isGranted: Boolean, permissionManager: PermissionsManager) {
        _hasMicPermission.value = isGranted

        if (!isGranted) {
            val message = if (permissionManager.shouldShowRationale()) {
                "Permission is not granted"
            } else {
                viewModelScope.launch { _eventFlow.emit(AudioTaleUiEvent.OpenAppSettings) }
                "Go to settings and grant microphone permission"
            }
            emitToast(message)
        }
    }

    fun closeDangerAlert(onNavigateBack: () -> Unit) {
        _showDangerAlert.value = false
        onNavigateBack()
    }

    fun resetDangerStatus() {
        _isDangerous.value = false
        _dangerousWords.value = emptyList()
    }

    fun checkAudioFileAndPlay(fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = audioFileStorage.getOrDownloadAudioFile(fileUrl)
            result.onSuccess { file ->
                playAudio(file)
            }.onFailure {
                emitToast("Upload audio failed")
            }
        }
    }

    fun playAudio(file: File) {
        stopPlayback()
        viewModelScope.launch {
            audioPlaybackController.play(file) {
                _isPaused.value = true
            }
            _isPaused.value = false
        }
    }

    fun stopPlayback() {
        audioPlaybackController.stop()
        _isPaused.value = false
    }

    private fun stopRecording(recorder: WAVAudioRecorder) {
        _audioFile.value = recorder.stopRecording()
        _isRecording.value = false
    }

    protected fun validateAudio(): Boolean {
        return if (_audioFile.value == null) {
            emitToast("At first record audio tale")
            false
        } else true
    }

    protected fun validateText(): Boolean {
        return if (_title.value.isBlank() || _description.value.isBlank()) {
            emitToast("Fill all fields")
            false
        } else true
    }

    protected fun requestMicPermission() {
        viewModelScope.launch {
            _eventFlow.emit(AudioTaleUiEvent.RequestPermission)
        }
    }

    protected fun emitToast(message: String) {
        viewModelScope.launch {
            _eventFlow.emit(AudioTaleUiEvent.ShowToast(message))
        }
    }
}
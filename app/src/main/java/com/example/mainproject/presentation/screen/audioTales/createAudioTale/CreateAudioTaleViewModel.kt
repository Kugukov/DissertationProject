package com.example.mainproject.presentation.screen.audioTales.createAudioTale

import androidx.lifecycle.viewModelScope
import com.example.mainproject.domain.repository.AudioTaleRepository
import com.example.mainproject.data.storage.AudioFileStorage
import com.example.mainproject.domain.DeviceInfoProvider
import com.example.mainproject.domain.model.AudioTale
import com.example.mainproject.presentation.screen.audioTales.BaseCreateEditAudioTaleViewModel
import com.example.mainproject.core.utils.workWithAudio.AudioPlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAudioTaleViewModel @Inject constructor(
    private val audioTaleRepository: AudioTaleRepository,
    private val audioPlaybackController: AudioPlaybackController,
    audioFileStorage: AudioFileStorage,
    private val deviceInfoProvider: DeviceInfoProvider
) : BaseCreateEditAudioTaleViewModel(audioPlaybackController, audioFileStorage = audioFileStorage) {

    fun submitNewAudioTale() {
        if (!validateAudio()) return
        if (!validateText()) return

        uploadAndReset()
    }

    private fun uploadAndReset() {
        viewModelScope.launch {
            val file = _audioFile.value ?: return@launch
            val tale = AudioTale(
                title = _title.value,
                description = _description.value,
                audioFile = file,
                audioDuration = audioPlaybackController.getAudioDuration(file)
            )
            val result = audioTaleRepository.uploadAudioTale(tale, deviceInfoProvider.getDeviceInfo()["device_id"] ?: "unknown")

            result.onSuccess { uploadResult ->
                _isDangerous.value = uploadResult.isDangerous
                _dangerousWords.value = uploadResult.dangerWords

                if (uploadResult.isDangerous) {
                    uploadResult.taleId?.let {
                        audioTaleRepository.deleteAudioTale(it)
                    }
                }

                _showDangerAlert.value = true
                resetState()
            }.onFailure {
                emitToast("Fail upload: ${it.message}")
            }
        }
    }

    fun resetState() {
        _title.value = ""
        _description.value = ""
        deleteTempAudio()
        stopPlayback()
        _isRecording.value = false
    }

}
package com.example.mainproject.ui.components.audioTales.createAudioTale

import androidx.lifecycle.viewModelScope
import com.example.mainproject.data.AudioTaleRepository
import com.example.mainproject.domain.AudioFileStorage
import com.example.mainproject.domain.DeviceInfoProvider
import com.example.mainproject.models.AudioTale
import com.example.mainproject.ui.components.audioTales.BaseCreateEditAudioTaleViewModel
import com.example.mainproject.utils.workWithAudio.AudioPlaybackController
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
            val result = audioTaleRepository.uploadAudioTale(tale, deviceInfoProvider.getDeviceId())

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
                emitToast("Ошибка загрузки: ${it.message}")
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
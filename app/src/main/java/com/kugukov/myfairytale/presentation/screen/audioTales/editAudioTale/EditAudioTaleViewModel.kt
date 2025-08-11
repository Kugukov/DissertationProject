package com.kugukov.myfairytale.presentation.screen.audioTales.editAudioTale

import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.domain.repository.AudioTaleRepository
import com.kugukov.myfairytale.data.storage.AudioFileStorage
import com.kugukov.myfairytale.presentation.screen.audioTales.BaseCreateEditAudioTaleViewModel
import com.kugukov.myfairytale.core.utils.workWithAudio.AudioPlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditAudioTaleViewModel @Inject constructor(
    private val audioTaleRepository: AudioTaleRepository,
    audioFileStorage: AudioFileStorage,
    private val audioPlaybackController: AudioPlaybackController,
) : BaseCreateEditAudioTaleViewModel(audioPlaybackController, audioFileStorage) {

    fun init(title: String, description: String, file: File) {
        _title.value = title
        _description.value = description
        _audioFile.value = file
    }

    fun submitEdit(taleId: Int) {
        if (!validateAudio()) return
        if (!validateText()) return

        updateAndReset(taleId)
    }

    private fun updateAndReset(taleId: Int) {
        viewModelScope.launch {
            val file = _audioFile.value ?: return@launch
            val result = audioTaleRepository.updateAudioTale(
                taleId = taleId,
                newTitle = _title.value,
                newDescription = _description.value,
                newAudioFile = file,
                newAudioDuration = audioPlaybackController.getAudioDuration(file)
            )

            result.onSuccess { updateResult ->
                _isDangerous.value = updateResult.isDangerous
                _dangerousWords.value = updateResult.dangerWords

                if (updateResult.isDangerous) {
                    audioTaleRepository.deleteAudioTale(taleId)
                }

                _showDangerAlert.value = true
                resetState()
            }.onFailure {
                emitToast("Update failed: ${it.message}")
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
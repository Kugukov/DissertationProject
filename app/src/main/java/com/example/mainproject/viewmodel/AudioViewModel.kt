package com.example.mainproject.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mainproject.ui.components.audioTales.AudioTale
import java.io.File

class AudioViewModel: ViewModel() {
    private val _audioRecords = mutableStateListOf<AudioTale>()
    val audioRecords: List<AudioTale> get() = _audioRecords

    fun loadAudioFiles(context: Context) {
        val audioDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return
        val files = audioDir.listFiles()?.filter { it.extension.lowercase() == "m4a" } ?: emptyList()
        _audioRecords.clear()
        files.forEachIndexed { index, file ->
            val record = AudioTale(
                audioTaleId = index + 1,
                audioFile = file,
                title = mutableStateOf(file.nameWithoutExtension),
                description = mutableStateOf(getAudioDuration(file)),
                audioDuration = mutableStateOf(getAudioDuration(file))
            )
            _audioRecords.add(record)
        }
    }

    fun updateAudioRecord(updatedRecord: AudioTale) {
        val index = _audioRecords.indexOfFirst { it.audioTaleId == updatedRecord.audioTaleId }
        if (index != -1) {
            _audioRecords[index] = updatedRecord
        }
    }

    @SuppressLint("DefaultLocale")
    fun getAudioDuration(file: File): String {
        val mediaPlayer = MediaPlayer()
        return try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            val ms = mediaPlayer.duration.toLong()
            val minutes = (ms / 1000) / 600
            val secs = (ms / 1000) % 600
            return String.format("%02d:%02d", minutes, secs)
        } catch (e: Exception) {
            ""
        } finally {
            mediaPlayer.release()
        }
    }

    fun removeAudioRecord(record: AudioTale) {
        if (record.audioFile.exists()) {
            val deleted = record.audioFile.delete()
            if (deleted) {
                _audioRecords.remove(record)
            }
        }
    }
}
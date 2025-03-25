package com.example.mainproject.viewmodel

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
            // TODO Дополнительно можно получить длительность через MediaPlayer, если нужно.
            val record = AudioTale(
                audioTaleId = index + 1,
                audioFile = file,
                title = mutableStateOf(file.nameWithoutExtension),
                description = mutableStateOf(" ")
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

    private fun getAudioDuration(file: File): Long {
        val mediaPlayer = MediaPlayer()
        return try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.duration.toLong()
        } catch (e: Exception) {
            0L
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
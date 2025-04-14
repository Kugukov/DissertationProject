package com.example.mainproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import java.io.File

@SuppressLint("StaticFieldLeak")
object AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    var isPaused = false

    /* TODO play/pause в каждом create/edit */
    fun playAudio(file: File, context: Context) {
        stopAudio()
        Log.d("MediaPlayer", "Воспроизведение: ${file.absolutePath}")

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()

                setOnCompletionListener {
                    stopAudio()
                }

                isPaused = false
            } catch (e: Exception) {
                Log.e("MediaPlayer", "Ошибка воспроизведения: ${e.message}")
                Toast.makeText(context, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun pauseAudio() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
        isPaused = true
    }

    fun resumeAudio() {
        mediaPlayer?.start()
        isPaused = false
    }

    fun stopAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPaused = false
    }
}
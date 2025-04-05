package com.example.mainproject.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import java.io.File

object AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(file: File, context: Context) {
        stopAudio() // Останавливаем предыдущее воспроизведение, если оно было
        Log.d("MediaPlayer", "Воспроизведение: ${file.absolutePath}")

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()

                setOnCompletionListener {
                    stopAudio()
                }
            } catch (e: Exception) {
                Log.e("MediaPlayer", "Ошибка воспроизведения: ${e.message}")
                Toast.makeText(context, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
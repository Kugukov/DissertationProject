package com.kugukov.myfairytale.core.utils.workWithAudio

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import javax.inject.Inject

class AudioPlayer @Inject constructor() : AudioPlaybackController {
    private var mediaPlayer: MediaPlayer? = null

    private var isPlaybackActive = false
    private var isPaused = false

    override fun play(file: File, onFinish: () -> Unit) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()
                isPlaybackActive = true
                isPaused = false

                setOnCompletionListener {
                    onFinish()
                    if (mediaPlayer?.isPlaying == true)
                        stop()
                }
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Ошибка воспроизведения: ${e.message}")
                stop()
            }
        }
    }

    override fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
        isPaused = true
    }

    override fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
        isPaused = false
    }

    override fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPaused = false
        isPlaybackActive = false
    }

    override fun isPlaying(): Boolean = isPlaybackActive

    override fun isPaused(): Boolean = isPaused

    @SuppressLint("DefaultLocale")
    override fun getAudioDuration(file: File): String {
        val mediaPlayer = MediaPlayer()
        return try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            val ms = mediaPlayer.duration.toLong()
            val minutes = (ms / 1000) / 60
            val secs = (ms / 1000) % 60
            String.format("%02d:%02d", minutes, secs)
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Ошибка при получении длительности: ${e.message}")
            "00:00"
        } finally {
            mediaPlayer.release()
        }
    }
}
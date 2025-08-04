package com.example.mainproject.utils.workWithAudio

import java.io.File

interface AudioPlaybackController {
    fun play(file: File, onFinish: () -> Unit)
    fun pause()
    fun resume()
    fun stop()
    fun isPlaying(): Boolean
    fun isPaused(): Boolean
    fun getAudioDuration(file: File): String
}
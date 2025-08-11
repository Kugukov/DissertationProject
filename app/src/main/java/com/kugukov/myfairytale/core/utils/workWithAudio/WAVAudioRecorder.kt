package com.kugukov.myfairytale.core.utils.workWithAudio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WAVAudioRecorder(private val context: Context) {
    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var isRecording = false
    private var audioFile: File? = null

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private fun getCurrentFormattedTime(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        return sdf.format(Date())
    }

    fun startRecording(fileName: String? = null) {
        val audioDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        if (audioDir != null && !audioDir.exists()) {
            audioDir.mkdirs()
        }

        val finalFileName = fileName ?: "audio_${getCurrentFormattedTime()}"
        audioFile = File(audioDir, "$finalFileName.wav")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )
        }

        audioRecord?.startRecording()
        isRecording = true

        val pcmData = ByteArray(bufferSize)

        recordingThread = Thread {
            FileOutputStream(audioFile).use { output ->
                val wavStream = ByteArrayOutputStream()
                while (isRecording) {
                    val read = audioRecord?.read(pcmData, 0, pcmData.size) ?: 0
                    if (read > 0) {
                        wavStream.write(pcmData, 0, read)
                    }
                }
                val audioBytes = wavStream.toByteArray()
                writeWavHeader(output, audioBytes.size.toLong())
                output.write(audioBytes)
            }
        }
        recordingThread?.start()
    }

    fun stopRecording(): File? {
        isRecording = false
        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
        recordingThread = null
        return audioFile
    }

    private fun writeWavHeader(out: OutputStream, totalAudioLen: Long) {
        val totalDataLen = totalAudioLen + 36
        val channels = 1
        val byteRate = 16 * sampleRate * channels / 8

        val header = ByteArray(44)

        // RIFF/WAVE header
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        writeInt(header, 4, totalDataLen.toInt())
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        writeInt(header, 16, 16) // Subchunk1Size
        writeShort(header, 20, 1) // AudioFormat = PCM
        writeShort(header, 22, channels.toShort())
        writeInt(header, 24, sampleRate)
        writeInt(header, 28, byteRate)
        writeShort(header, 32, (channels * 16 / 8).toShort()) // BlockAlign
        writeShort(header, 34, 16) // BitsPerSample
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        writeInt(header, 40, totalAudioLen.toInt())

        out.write(header, 0, 44)
    }

    private fun writeInt(header: ByteArray, offset: Int, value: Int) {
        header[offset] = (value and 0xff).toByte()
        header[offset + 1] = (value shr 8 and 0xff).toByte()
        header[offset + 2] = (value shr 16 and 0xff).toByte()
        header[offset + 3] = (value shr 24 and 0xff).toByte()
    }

    private fun writeShort(header: ByteArray, offset: Int, value: Short) {
        header[offset] = (value.toInt() and 0xff).toByte()
        header[offset + 1] = (value.toInt() shr 8 and 0xff).toByte()
    }
}
package com.example.mainproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceInfo {
    @SuppressLint("HardwareIds")
    fun getDeviceInfo(context: Context): Map<String, String> {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        val registrationTime = System.currentTimeMillis().toString()

        return mapOf(
            "device_id" to androidId,
            "model" to model,
            "os_version" to osVersion,
            "registration_time" to registrationTime
        )
    }
}
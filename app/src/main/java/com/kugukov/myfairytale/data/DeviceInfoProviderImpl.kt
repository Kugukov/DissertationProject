package com.kugukov.myfairytale.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.kugukov.myfairytale.domain.DeviceInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceInfoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceInfoProvider {
    @SuppressLint("HardwareIds")
    override fun getDeviceInfo(): Map<String, String> {
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
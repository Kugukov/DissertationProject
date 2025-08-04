package com.example.mainproject.domain

import android.content.Context
import com.example.mainproject.utils.DeviceInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceInfoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceInfoProvider {
    override fun getDeviceId(): String {
        return DeviceInfo.getDeviceInfo(context)["device_id"] ?: "unknown"
    }
}
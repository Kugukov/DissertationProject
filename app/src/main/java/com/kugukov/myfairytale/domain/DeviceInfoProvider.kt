package com.kugukov.myfairytale.domain

interface DeviceInfoProvider {
    fun getDeviceInfo(): Map<String, String>
}
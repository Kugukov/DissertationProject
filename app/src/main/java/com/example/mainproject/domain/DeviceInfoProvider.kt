package com.example.mainproject.domain

interface DeviceInfoProvider {
    fun getDeviceInfo(): Map<String, String>
}
package com.kugukov.myfairytale.domain.repository

interface DeviceRepository {
    suspend fun checkDeviceData(): Result<Boolean>
    suspend fun registerDevice(): Result<Unit>
}
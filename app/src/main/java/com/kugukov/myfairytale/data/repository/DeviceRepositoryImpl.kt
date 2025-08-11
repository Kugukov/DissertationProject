package com.kugukov.myfairytale.data.repository

import com.kugukov.myfairytale.data.network.ApiService
import com.kugukov.myfairytale.domain.DeviceInfoProvider
import com.kugukov.myfairytale.domain.repository.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val deviceInfoProvider: DeviceInfoProvider
) : DeviceRepository {

    override suspend fun checkDeviceData(): Result<Boolean> {
        return try {
            val deviceId = deviceInfoProvider.getDeviceInfo()["device_id"] ?: return Result.failure(
                IllegalStateException("Device ID is null")
            )

            val response = apiService.checkRegisterDevice(deviceId)

            if (response.isSuccessful) {
                Result.success(response.body()?.exists ?: false)
            } else {
                Result.failure(Exception("Check register device error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerDevice(): Result<Unit> {
        return try {
            val deviceInfo = deviceInfoProvider.getDeviceInfo()
            val response = apiService.registerDevice(deviceInfo)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Register device error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
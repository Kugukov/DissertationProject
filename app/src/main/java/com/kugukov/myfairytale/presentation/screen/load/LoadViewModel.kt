package com.kugukov.myfairytale.presentation.screen.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _checkDeviceDataSuccess = MutableStateFlow<Boolean?>(null)
    val checkDeviceDataSuccess: StateFlow<Boolean?> = _checkDeviceDataSuccess

    fun checkDeviceData() {
        viewModelScope.launch {
            val result = deviceRepository.checkDeviceData()

            result.onSuccess { exists ->
                _checkDeviceDataSuccess.value = exists
            }.onFailure {
                registerDeviceData()
            }
        }
    }

    fun registerDeviceData() {
        viewModelScope.launch {
            deviceRepository.registerDevice()
                .onSuccess {
                    _checkDeviceDataSuccess.value = true
                    Log.i("Device registration", "Registration done")
                }
                .onFailure {
                    Log.e(
                        "Device registration",
                        "Registration error: ${it.message}"
                    )
                }
        }
    }
}
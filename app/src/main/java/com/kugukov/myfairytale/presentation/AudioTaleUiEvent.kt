package com.kugukov.myfairytale.presentation

sealed class AudioTaleUiEvent {
    data class ShowToast(val message: String) : AudioTaleUiEvent()
    data object RequestPermission : AudioTaleUiEvent()
    data object OpenAppSettings : AudioTaleUiEvent()
}
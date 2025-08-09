package com.example.mainproject.presentation

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data object RequestPermission : UiEvent()
    data object OpenAppSettings : UiEvent()
}
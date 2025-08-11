package com.kugukov.myfairytale.presentation.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class UserRole {
    PARENT,
    CHILD
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _userRole = MutableStateFlow(UserRole.CHILD)
    val userRole: StateFlow<UserRole> = _userRole

    private val _showExitAccountAlert = MutableStateFlow(false)
    val showExitAccountAlert: StateFlow<Boolean> = _showExitAccountAlert

    private val _showSaveTaleAlert = MutableStateFlow(false)
    val showSaveTaleAlert: StateFlow<Boolean> = _showSaveTaleAlert

    fun openExitAccountAlert() {
        _showExitAccountAlert.value = true
    }

    fun closeExitAccountAlert() {
        _showExitAccountAlert.value = false
    }

    fun confirmExitAccountAlert(onNavigateToHome: () -> Unit) {
        run { onNavigateToHome() }
        _showExitAccountAlert.value = false
    }

    fun openSaveTaleAlert() {
        _showSaveTaleAlert.value = true
    }

    fun closeSaveTaleAlert(onNavigateBack: () -> Unit) {
        _showSaveTaleAlert.value = false
        run { onNavigateBack() }
    }
}
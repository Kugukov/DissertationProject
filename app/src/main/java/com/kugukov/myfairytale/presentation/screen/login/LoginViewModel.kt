package com.kugukov.myfairytale.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.data.repository.UserProfileRepository
import com.kugukov.myfairytale.data.repository.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    val savedChildUriImage = userProfileRepository.savedChildUriImage
    val savedParentUriImage = userProfileRepository.savedParentUriImage

    private val _selectedRole = MutableStateFlow(UserRole.CHILD)

    val selectedRole: StateFlow<UserRole> = _selectedRole.asStateFlow()
    private val _enteredPassword = MutableStateFlow("")

    val enteredPassword: StateFlow<String> = _enteredPassword.asStateFlow()
    private val _showPasswordDialog = MutableStateFlow(false)

    val showPasswordDialog: StateFlow<Boolean> = _showPasswordDialog.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent.asSharedFlow()
    sealed class LoginUiEvent {
        object NavigateToAudio : LoginUiEvent()
        object NavigateToOptions : LoginUiEvent()
        object ExitApp: LoginUiEvent()
        data class ShowToast(val message: String) : LoginUiEvent()
    }

    init {
        ensureImagesLoaded()
    }

    fun selectChild() {
        _selectedRole.value = UserRole.CHILD
        updateEnteredPassword("")
    }

    fun selectParent() {
        _selectedRole.value = UserRole.PARENT
    }

    fun updateEnteredPassword(value: String) {
        _enteredPassword.value = value
    }

    fun checkPassword(password: String): Boolean = userProfileRepository.checkPassword(password)

    fun onSetPasswordClick() {
        viewModelScope.launch {
            _showPasswordDialog.value = false
            _uiEvent.emit(LoginUiEvent.NavigateToOptions)
        }
    }
    fun onLaterSetPasswordClick() {
        viewModelScope.launch {
            _showPasswordDialog.value = false
            _uiEvent.emit(LoginUiEvent.NavigateToAudio)
        }
    }

    fun dismissPasswordDialog() {
        _showPasswordDialog.value = false
    }

    fun exitApp() {
        viewModelScope.launch {
            _uiEvent.emit(LoginUiEvent.ExitApp)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            if (_selectedRole.value == UserRole.PARENT) {
                if (userProfileRepository.savedPassword.value.isEmpty()) {
                    _showPasswordDialog.value = true
                } else {
                    if (checkPassword(_enteredPassword.value)) {
                        userProfileRepository.setCurrentUserRole(UserRole.PARENT)
                        _uiEvent.emit(LoginUiEvent.NavigateToAudio)
                    } else {
                        _uiEvent.emit(LoginUiEvent.ShowToast("Incorrect password"))
                    }
                }
            } else {
                userProfileRepository.setCurrentUserRole(UserRole.CHILD)
                _uiEvent.emit(LoginUiEvent.NavigateToAudio)
            }
        }
    }

    fun ensureImagesLoaded() {
        viewModelScope.launch {
            userProfileRepository.loadImage("saved_child_image.jpg")
            userProfileRepository.loadImage("saved_parent_image.jpg")
        }
    }
}
package com.kugukov.myfairytale.presentation.screen.option

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kugukov.myfairytale.data.repository.UserProfileRepository
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
class OptionsViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    val tempChildUriImage = userProfileRepository.tempChildUriImage
    val tempParentUriImage = userProfileRepository.tempParentUriImage
    val savedChildUriImage = userProfileRepository.savedChildUriImage
    val savedParentUriImage = userProfileRepository.savedParentUriImage
    private val _enteredPassword = MutableStateFlow("")
    val enteredPassword: StateFlow<String> = _enteredPassword.asStateFlow()

    private val _uiEvent = MutableSharedFlow<OptionsUiEvent>()
    val uiEvent: SharedFlow<OptionsUiEvent> = _uiEvent.asSharedFlow()
    sealed class OptionsUiEvent {
        object NavigateBack : OptionsUiEvent()
        data class ShowToast(val message: String) : OptionsUiEvent()
    }

    init {
        ensureImagesLoaded()
    }

    fun setTempChildUriImage(uri: Uri?) = userProfileRepository.setTempChildUriImage(uri)
    fun setTempParentUriImage(uri: Uri?) = userProfileRepository.setTempParentUriImage(uri)

    fun updateEnteredPassword(value: String) {
        _enteredPassword.value = value
    }

    fun onBackArrowClick() {
        viewModelScope.launch {
            _uiEvent.emit(OptionsUiEvent.NavigateBack)
        }
    }

    fun applyNewSettings() {
        if (!enteredPassword.value.isEmpty()) {
            userProfileRepository.setNewPassword(enteredPassword.value)
        }

        userProfileRepository.tempChildUriImage.value?.let { uri ->
            userProfileRepository.saveImageToInternalStorage(uri, "saved_child_image.jpg")
            setTempChildUriImage(null)
        }
        userProfileRepository.tempParentUriImage.value?.let { uri ->
            userProfileRepository.saveImageToInternalStorage(uri, "saved_parent_image.jpg")
            setTempParentUriImage(null)
        }
        viewModelScope.launch {
            _uiEvent.emit(OptionsUiEvent.ShowToast("Changes applied"))
            _uiEvent.emit(OptionsUiEvent.NavigateBack)
        }
    }

    fun ensureImagesLoaded() {
        viewModelScope.launch {
            userProfileRepository.loadImage("saved_child_image.jpg")
            userProfileRepository.loadImage("saved_parent_image.jpg")
        }
    }
}
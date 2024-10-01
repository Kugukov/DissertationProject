package com.example.mainproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    var resultText by mutableStateOf("Try")

    fun performAction() {
        resultText = "Action Performed"
    }
}
package com.example.mainproject.models

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun savePassword(password: String) {
        sharedPreferences.edit().putString("password_key", password).apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password_key", "") ?: "123"
    }

    fun disableFirstLaunch() {
        sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
    }
    fun getFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("is_first_launch", true)
    }

    /*TODO сохранение списка текстовых сказок*/
}
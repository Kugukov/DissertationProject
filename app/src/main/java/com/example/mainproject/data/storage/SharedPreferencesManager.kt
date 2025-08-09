package com.example.mainproject.data.storage

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun savePassword(password: String) {
        sharedPreferences.edit { putString("password_key", password) }
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password_key", "") ?: "123"
    }
}
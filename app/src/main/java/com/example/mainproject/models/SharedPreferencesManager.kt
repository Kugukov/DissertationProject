package com.example.mainproject.models

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun savePassword(password: String) {
        sharedPreferences.edit().putString("password_key", password).apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password_key", "") ?: "123"
    }

}
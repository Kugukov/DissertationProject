package com.example.mainproject.core.config

import com.example.mainproject.data.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val ON_MOBILE_URL = "http://192.168.164.249:5000/"

    fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(ON_MOBILE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
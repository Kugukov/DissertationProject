package com.example.mainproject.utils

import com.example.mainproject.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "http://192.168.128.249:5000/"
    private const val HOME_URL = "http://192.168.100.52:5000/"

    fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(HOME_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
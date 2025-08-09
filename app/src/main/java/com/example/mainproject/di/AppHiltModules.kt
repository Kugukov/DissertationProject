package com.example.mainproject.di

import com.example.mainproject.data.storage.AudioFileStorage
import com.example.mainproject.data.storage.AudioFileStorageImpl
import com.example.mainproject.domain.DeviceInfoProvider
import com.example.mainproject.data.DeviceInfoProviderImpl
import com.example.mainproject.domain.repository.AudioTaleRepository
import com.example.mainproject.data.repository.AudioTaleRepositoryImpl
import com.example.mainproject.data.network.ApiService
import com.example.mainproject.core.config.ApiConfig
import com.example.mainproject.core.utils.workWithAudio.AudioPlaybackController
import com.example.mainproject.core.utils.workWithAudio.AudioPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppHiltModule {

    @Binds
    abstract fun bindDeviceInfoProvider(
        impl: DeviceInfoProviderImpl
    ): DeviceInfoProvider

    @Binds
    abstract fun bindTaleRepository(
        impl: AudioTaleRepositoryImpl
    ): AudioTaleRepository

    @Binds
    abstract fun bindAudioFileStorage(
        impl: AudioFileStorageImpl
    ): AudioFileStorage
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiService(): ApiService {
        return ApiConfig.createApiService()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    @Singleton
    fun provideAudioPlaybackController(): AudioPlaybackController {
        return AudioPlayer()
    }
}


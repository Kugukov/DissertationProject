package com.kugukov.myfairytale.di

import android.content.Context
import com.kugukov.myfairytale.data.storage.AudioFileStorage
import com.kugukov.myfairytale.data.storage.AudioFileStorageImpl
import com.kugukov.myfairytale.domain.DeviceInfoProvider
import com.kugukov.myfairytale.data.DeviceInfoProviderImpl
import com.kugukov.myfairytale.domain.repository.AudioTaleRepository
import com.kugukov.myfairytale.data.repository.AudioTaleRepositoryImpl
import com.kugukov.myfairytale.data.network.ApiService
import com.kugukov.myfairytale.core.config.ApiConfig
import com.kugukov.myfairytale.core.utils.workWithAudio.AudioPlaybackController
import com.kugukov.myfairytale.core.utils.workWithAudio.AudioPlayer
import com.kugukov.myfairytale.data.repository.DeviceRepositoryImpl
import com.kugukov.myfairytale.data.repository.UserProfileRepository
import com.kugukov.myfairytale.domain.repository.DeviceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    abstract fun bindAudioTaleRepository(
        impl: AudioTaleRepositoryImpl
    ): AudioTaleRepository

    @Binds
    abstract fun bindDeviceRepository(
        impl: DeviceRepositoryImpl
    ): DeviceRepository

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

@Module
@InstallIn(SingletonComponent::class)
object UserProfileRepositoryModule {

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        @ApplicationContext context: Context
    ): UserProfileRepository {
        return UserProfileRepository(context)
    }
}



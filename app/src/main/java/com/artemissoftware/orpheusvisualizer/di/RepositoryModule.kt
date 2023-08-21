package com.artemissoftware.orpheusvisualizer.di

import com.artemissoftware.orpheusvisualizer.data.repository.AudioPlayerRepositoryImpl
import com.artemissoftware.orpheusvisualizer.domain.repository.AudioPlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHeadphonePlayerRepository(repository: AudioPlayerRepositoryImpl): AudioPlayerRepository
}

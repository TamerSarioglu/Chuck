package com.tamersarioglu.chuck.di

import com.tamersarioglu.chuck.data.repository.ChuckRepositoryImpl
import com.tamersarioglu.chuck.domain.repository.ChuckRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideJokeRepository(
       chuckRepositoryImpl: ChuckRepositoryImpl
    ): ChuckRepository {
        return chuckRepositoryImpl
    }
}
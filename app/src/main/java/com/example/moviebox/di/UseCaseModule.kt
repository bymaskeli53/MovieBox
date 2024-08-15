package com.example.moviebox.di

import com.example.moviebox.domain.FormatDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideFormatDateUseCase(): FormatDateUseCase = FormatDateUseCase()
}
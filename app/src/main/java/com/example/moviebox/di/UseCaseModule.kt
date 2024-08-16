package com.example.moviebox.di

import com.example.moviebox.domain.FormatDateUseCase
import com.example.moviebox.domain.GetFavoriteMovieIDsUseCase
import com.example.moviebox.repository.MovieRepository
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

    @Provides
    @Singleton
    fun getFavoriteMovieIDsUseCase(movieRepository: MovieRepository): GetFavoriteMovieIDsUseCase =
        GetFavoriteMovieIDsUseCase(movieRepository)
}

package com.example.moviebox.di

import android.content.Context
import androidx.room.Room
import com.example.moviebox.database.MovieDatabase
import com.example.moviebox.util.constant.DatabaseConstants.MOVIE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase = Room.databaseBuilder(context, MovieDatabase::class.java, MOVIE_DATABASE).build()

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase) = database.movieDao()
}

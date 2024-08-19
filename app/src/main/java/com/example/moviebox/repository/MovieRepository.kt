package com.example.moviebox.repository

import androidx.paging.PagingData
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.Actors
import com.example.moviebox.model.MovieListResponse
import com.example.moviebox.model.MovieItem
import com.example.moviebox.model.TrailerResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<MovieItem>>

    suspend fun getMovieCredits(movieId: Int): Actors

    suspend fun getMovieTrailers(movieId: Int): TrailerResponse

    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    suspend fun insertFavoriteMovie(movieEntity: MovieEntity)

    suspend fun deleteFavoriteMovie(movieEntity: MovieEntity)

    suspend fun updateFavoriteStatus(movieEntity: MovieEntity)

    suspend fun searchMovies(query: String): MovieListResponse

    suspend fun getMovieById(movieId: Int): MovieEntity?

    fun getFavoriteMovieIDs(): Flow<List<Int>>

    fun searchMovies2(query: String): Flow<PagingData<MovieItem>>
}

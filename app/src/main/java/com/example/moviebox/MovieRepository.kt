package com.example.moviebox

import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.TrailerResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(): Movie

    suspend fun getMovieCredits(movieId: Int): Actors

    suspend fun getMovieTrailers(movieId: Int): TrailerResponse

    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    suspend fun insertFavoriteMovie(movieEntity: MovieEntity)

    suspend fun deleteFavoriteMovie(movieEntity: MovieEntity)

    suspend fun updateFavoriteStatus(movieEntity: MovieEntity)

    suspend fun searchMovies(query: String): Movie


}

package com.example.moviebox

import androidx.paging.PagingData
import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result
import com.example.moviebox.model.TrailerResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<Result>>

    suspend fun getMovieCredits(movieId: Int): Actors

    suspend fun getMovieTrailers(movieId: Int): TrailerResponse

    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    suspend fun insertFavoriteMovie(movieEntity: MovieEntity)

    suspend fun deleteFavoriteMovie(movieEntity: MovieEntity)

    suspend fun updateFavoriteStatus(movieEntity: MovieEntity)

    suspend fun searchMovies(query: String): Movie

    suspend fun getMovieById(movieId: Int): MovieEntity?

     fun getFavoriteMovieIds(): Flow<List<Int>>

     fun searchMovies2(query: String): Flow<PagingData<Result>>
}

package com.example.moviebox

import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.TrailerResponse
import com.example.moviebox.remote.MovieApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
        private val movieDao: MovieDao,
    ) : MovieRepository {
        override suspend fun getPopularMovies(): Movie = movieApi.getPopularMovies()

        override suspend fun getMovieCredits(movieId: Int): Actors = movieApi.getMovieCredits(movieId)

        override suspend fun getMovieTrailers(movieId: Int): TrailerResponse = movieApi.getMovieTrailers(movieId)

        override fun getFavoriteMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovies()

        override suspend fun insertFavoriteMovie(movieEntity: MovieEntity) {
            movieDao.insertMovie(movieEntity)
        }

        override suspend fun deleteFavoriteMovie(movieEntity: MovieEntity) {
            movieDao.deleteMovie(movieEntity)
        }
    }

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

        override fun getFavoriteMovies(): Flow<List<MovieEntity>> = movieDao.getFavoriteMovies()

        override suspend fun insertFavoriteMovie(movieEntity: MovieEntity) {
            movieDao.insertMovie(movieEntity)
        }

        override suspend fun deleteFavoriteMovie(movieEntity: MovieEntity) {
            movieDao.deleteMovie(movieEntity)
        }

        override suspend fun updateFavoriteStatus(movieEntity: MovieEntity) {
            val movie = movieDao.getMovieById(movieEntity.id)

            if (movie != null) {
                movie.isFavorite = !movie.isFavorite
                movieDao.update(movie)
            } else {
                movieEntity.isFavorite = true
                movieDao.insertMovie(movieEntity)
            }
        }
    }

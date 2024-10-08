package com.example.moviebox.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviebox.database.MovieDao
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.Actors
import com.example.moviebox.model.MovieListResponse
import com.example.moviebox.model.MovieItem
import com.example.moviebox.model.TrailerResponse
import com.example.moviebox.paging.MoviePagingSource
import com.example.moviebox.paging.SearchPagingSource
import com.example.moviebox.remote.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
        private val movieDao: MovieDao,
    ) : MovieRepository {
        override fun getPopularMovies(): Flow<PagingData<MovieItem>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = true,
                    ),
                pagingSourceFactory = { MoviePagingSource(movieApi) },
            ).flow


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

        override suspend fun searchMovies(query: String): MovieListResponse = movieApi.searchMovies(query = query)

        override suspend fun getMovieById(movieId: Int): MovieEntity? =
            withContext(Dispatchers.IO) {
                movieDao.getMovieById(movieId)
            }

        override fun getFavoriteMovieIDs(): Flow<List<Int>> = movieDao.getFavoriteMovieIDs()

        override fun searchMovies2(query: String): Flow<PagingData<MovieItem>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = { SearchPagingSource(movieApi, query) },
            ).flow
    }

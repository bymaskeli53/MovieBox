package com.example.moviebox

import com.example.moviebox.model.Movie
import com.example.moviebox.remote.MovieApi
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
    ) : MovieRepository {
        override suspend fun getPopularMovies(): Movie = movieApi.getPopularMovies()
    }

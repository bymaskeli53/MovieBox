package com.example.moviebox

import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.TrailerResponse
import com.example.moviebox.remote.MovieApi
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieApi: MovieApi,
    ) : MovieRepository {
        override suspend fun getPopularMovies(): Movie = movieApi.getPopularMovies()

        override suspend fun getMovieCredits(movieId: Int): Actors = movieApi.getMovieCredits(movieId)

        override suspend fun getMovieTrailers(movieId: Int): TrailerResponse = movieApi.getMovieTrailers(movieId)
    }

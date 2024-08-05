package com.example.moviebox

import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.TrailerResponse

interface MovieRepository {
    suspend fun getPopularMovies(): Movie

    suspend fun getMovieCredits(movieId: Int): Actors

    suspend fun getMovieTrailers(movieId: Int): TrailerResponse
}

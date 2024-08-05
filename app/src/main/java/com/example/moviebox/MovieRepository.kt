package com.example.moviebox

import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): Movie

    suspend fun getMovieCredits(movieId: Int): Actors
}

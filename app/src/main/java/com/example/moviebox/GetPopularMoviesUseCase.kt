package com.example.moviebox

import com.example.moviebox.model.Movie
import javax.inject.Inject

class GetPopularMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(): Movie = movieRepository.getPopularMovies()
    }

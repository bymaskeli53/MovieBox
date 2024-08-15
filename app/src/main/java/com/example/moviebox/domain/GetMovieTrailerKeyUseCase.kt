package com.example.moviebox.domain

import com.example.moviebox.repository.MovieRepository
import javax.inject.Inject

class GetMovieTrailerKeyUseCase
    @Inject
    constructor(
        val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(movieId: Int): String? {
            val trailers = movieRepository.getMovieTrailers(movieId)
            return trailers.results.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key
        }
    }

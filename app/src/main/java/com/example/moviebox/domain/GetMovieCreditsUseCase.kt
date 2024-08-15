package com.example.moviebox.domain

import com.example.moviebox.repository.MovieRepository
import com.example.moviebox.model.Actors
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Actors = movieRepository.getMovieCredits(movieId)

}
package com.example.moviebox.domain

import com.example.moviebox.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieIDsUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(): Flow<List<Int>> = movieRepository.getFavoriteMovieIDs()
    }

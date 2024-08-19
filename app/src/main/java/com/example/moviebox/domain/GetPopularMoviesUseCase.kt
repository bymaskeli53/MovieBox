package com.example.moviebox.domain

import androidx.paging.PagingData
import com.example.moviebox.model.MovieItem
import com.example.moviebox.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(): Flow<PagingData<MovieItem>> = movieRepository.getPopularMovies()
    }

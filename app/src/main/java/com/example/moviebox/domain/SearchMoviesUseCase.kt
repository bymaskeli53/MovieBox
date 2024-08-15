package com.example.moviebox.domain

import androidx.paging.PagingData
import com.example.moviebox.model.Result
import com.example.moviebox.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(query: String): Flow<PagingData<Result>> = movieRepository.searchMovies2(query)
    }

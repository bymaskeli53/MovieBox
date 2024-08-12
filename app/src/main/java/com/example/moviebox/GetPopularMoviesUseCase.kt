package com.example.moviebox

import androidx.paging.PagingData
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(): Flow<PagingData<Result>> = movieRepository.getPopularMovies()
    }

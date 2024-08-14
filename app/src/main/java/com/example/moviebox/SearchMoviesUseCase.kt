package com.example.moviebox

import androidx.paging.PagingData
import com.example.moviebox.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(query: String): Flow<PagingData<Result>> {
        return movieRepository.searchMovies2(query)
    }
}
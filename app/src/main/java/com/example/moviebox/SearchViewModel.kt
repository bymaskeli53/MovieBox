package com.example.moviebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result
import com.example.moviebox.util.FormatDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        val repository: MovieRepository,
        private val formatDateUseCase: FormatDateUseCase,
    ) : ViewModel() {
        private val _movies = MutableStateFlow<Resource<Movie>>(Resource.Idle)
        val movies: StateFlow<Resource<Movie>> = _movies

        private var isLoading = false

        fun searchMovies(query: String) {
            if (isLoading) return
            isLoading = true

            viewModelScope.launch {
                _movies.update { Resource.Loading }
                isLoading = true
                try {
                    val response = repository.searchMovies(query)
                    _movies.update { Resource.Success(response) }
                    // _movies.value = response.results
                } catch (e: Exception) {
                    _movies.update { Resource.Error(exception = e) }
                    // TODO: will be handled later
                } finally {
                    isLoading = false
                }
            }
        }

        fun formatDate(inputDate: String): String = formatDateUseCase(inputDate)

    fun searchMovies2(query: String): Flow<PagingData<Result>> {
        return repository.searchMovies2(query)
            .cachedIn(viewModelScope)
    }
    }

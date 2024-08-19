package com.example.moviebox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviebox.domain.FormatDateUseCase
import com.example.moviebox.domain.SearchMoviesUseCase
import com.example.moviebox.model.MovieListResponse
import com.example.moviebox.model.MovieItem
import com.example.moviebox.repository.MovieRepository
import com.example.moviebox.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        val repository: MovieRepository,
        private val formatDateUseCase: FormatDateUseCase,
        private val searchMoviesUseCase: SearchMoviesUseCase,
    ) : ViewModel() {
        private val _movies = MutableStateFlow<Resource<MovieListResponse>>(Resource.Idle)
        val movies: StateFlow<Resource<MovieListResponse>> = _movies

        private val _searchQuery = MutableStateFlow("")

        val moviesFlow: Flow<PagingData<MovieItem>> =
            _searchQuery
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        repository.getPopularMovies()
                    } else {
                        repository.searchMovies2(query)
                    }
                }.cachedIn(viewModelScope)

        private val _searchResults = MutableStateFlow<PagingData<MovieItem>>(PagingData.empty())
        val searchResults: StateFlow<PagingData<MovieItem>> get() = _searchResults

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

                } finally {
                    isLoading = false
                }
            }
        }

        fun formatDate(inputDate: String): String = formatDateUseCase(inputDate)
    }

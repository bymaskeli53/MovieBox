package com.example.moviebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
    @Inject
    constructor(
        private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    ) : ViewModel() {
        private var isLoading = false

        private val _movies = MutableStateFlow<Resource<Movie>>(Resource.Loading())
        val movies: StateFlow<Resource<Movie>> = _movies

        fun getPopularMovies() {
            if (isLoading) return
            isLoading = true
            viewModelScope.launch {
                _movies.update { Resource.Loading() }
                isLoading = true
                try {
                    val result = getPopularMoviesUseCase()
                    //  val currentData = (_movies.value as? Resource.Success)?.data
                    _movies.update { Resource.Success(result) }
                } catch (e: Exception) {
                    _movies.update { Resource.Error(e.message.toString()) }
                } finally {
                    isLoading = false
                }
            }
        }
    }

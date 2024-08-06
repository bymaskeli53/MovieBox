package com.example.moviebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
    @Inject
    constructor(
        val movieRepository: MovieRepository,
    ) : ViewModel() {
        private val _favoriteMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
        val favoriteMovies: StateFlow<List<MovieEntity>> get() = _favoriteMovies

        init {
            getFavoriteMovies()
        }

        private fun getFavoriteMovies() {
            viewModelScope.launch {
                movieRepository.getFavoriteMovies().collect {
                    _favoriteMovies.value = it
                }
            }
        }

        fun insertFavoriteMovie(movie: MovieEntity) {
            viewModelScope.launch {
                movieRepository.insertFavoriteMovie(movie)
                // TODO: Learn is this best practice
            }
        }

        fun deleteFavoriteMovie(movie: MovieEntity) {
            viewModelScope.launch {
                movieRepository.deleteFavoriteMovie(movie)
            }
        }
    }

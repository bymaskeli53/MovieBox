package com.example.moviebox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.database.MovieEntity
import com.example.moviebox.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
    @Inject
    constructor(
        val movieRepository: MovieRepository,
    ) : ViewModel() {
        private val _favoriteMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
        val favoriteMovies: StateFlow<List<MovieEntity>> get() = _favoriteMovies

        private val _currentMovie = MutableStateFlow<MovieEntity?>(null)
        val currentMovie: StateFlow<MovieEntity?> = _currentMovie

        init {
            getFavoriteMovies()
        }

        fun getFavoriteMovies() {
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

        fun onFavoriteButtonClick(movie: MovieEntity) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    movieRepository.updateFavoriteStatus(movie)
                }
            }
        }

        fun getMovieById(movieId: Int) {
            viewModelScope.launch {
                _currentMovie.value = movieRepository.getMovieById(movieId)
            }
        }
    }

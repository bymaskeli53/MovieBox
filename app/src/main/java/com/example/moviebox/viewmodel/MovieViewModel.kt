package com.example.moviebox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviebox.repository.MovieRepository
import com.example.moviebox.domain.GetMovieTrailerKeyUseCase
import com.example.moviebox.domain.GetPopularMoviesUseCase
import com.example.moviebox.model.Result
import com.example.moviebox.domain.FormatDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
    @Inject
    constructor(
        private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
        private val formatDateUseCase: FormatDateUseCase,
        private val getMovieTrailerKeyUseCase: GetMovieTrailerKeyUseCase,
        private val movieRepository: MovieRepository,
    ) : ViewModel() {

    private val _movies = MutableStateFlow<PagingData<Result>>(PagingData.empty())
    val movies: StateFlow<PagingData<Result>> = _movies.asStateFlow()

    init {
        refreshMovies()
    }

//        // PagingData for the popular movies
//        val movies: StateFlow<PagingData<Result>> =
//            movieRepository
//                .getPopularMovies()
//                .cachedIn(viewModelScope)
//                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

        // StateFlow for favorite movie IDs
        private val _favoriteMovieIds = MutableStateFlow<List<Int>>(emptyList())
        val favoriteMovieIds: StateFlow<List<Int>> = _favoriteMovieIds

        // LiveData for trailer key
        private val _trailerKey = MutableLiveData<String?>()
        val trailerKey: LiveData<String?> get() = _trailerKey

        // StateFlow for tracking the position of the selected item
        private val _position = MutableStateFlow(0)
        val position: StateFlow<Int> get() = _position

        // StateFlow for grid layout toggle
        private val _isGridLayout = MutableStateFlow(false)
        val isGridLayout: StateFlow<Boolean> get() = _isGridLayout

        // Format the date using the injected use case
        fun formatDate(inputDate: String): String = formatDateUseCase(inputDate)

        // Fetch trailer key based on the movie ID
        fun fetchTrailerKey(movieId: Int) {
            viewModelScope.launch {
                _trailerKey.value = getMovieTrailerKeyUseCase(movieId)
            }
        }

        // Retrieve favorite movie IDs and update the StateFlow
        fun getFavoriteMovieIds() {
            viewModelScope.launch {
                movieRepository.getFavoriteMovieIds().collect { ids ->
                    _favoriteMovieIds.value = ids
                }
            }
        }

        // Set the position of the selected item
        fun setItemPosition(position: Int) {
            _position.value = position
        }

        // Toggle grid layout
        fun setGridLayout(isGridLayout: Boolean) {
            _isGridLayout.value = isGridLayout
        }

    fun refreshMovies() {
        viewModelScope.launch {
            movieRepository.getPopularMovies()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _movies.value = pagingData
                }
        }
    }

    }

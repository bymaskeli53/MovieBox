package com.example.moviebox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.model.Movie
import com.example.moviebox.util.FormatDateUseCase
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
        private val formatDateUseCase: FormatDateUseCase,
        private val getMovieTrailerKeyUseCase: GetMovieTrailerKeyUseCase
    ) : ViewModel() {
        private var isLoading = false

        private val _movies = MutableStateFlow<Resource<Movie>>(Resource.Loading())
        val movies: StateFlow<Resource<Movie>> = _movies

    private val _trailerKey = MutableLiveData<String?>()
    val trailerKey: LiveData<String?> get() = _trailerKey

        private val _position = MutableStateFlow<Int>(0)
        val position: StateFlow<Int> get() = _position

        private val _isGridLayout = MutableStateFlow<Boolean>(false)
        val isGridLayout: StateFlow<Boolean> get() = _isGridLayout

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
                    _movies.update { Resource.Error(e.localizedMessage.toString()) }
                } finally {
                    isLoading = false
                }
            }
        }

        fun setItemPosition(position: Int) {
            _position.value = position
        }

        fun setGridLayout(isGridLayout: Boolean) {
            _isGridLayout.value = isGridLayout
        }

        fun formatDate(inputDate: String): String = formatDateUseCase(inputDate)


    fun fetchTrailerKey(movieId: Int) {
        viewModelScope.launch {
            val key = getMovieTrailerKeyUseCase(movieId)
            _trailerKey.value = key
        }
        }


    }

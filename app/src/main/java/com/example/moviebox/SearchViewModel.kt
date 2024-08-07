package com.example.moviebox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class SearchViewModel @Inject constructor(val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow<Resource<Movie>>(Resource.Loading())
    val movies: StateFlow<Resource<Movie>> = _movies

    private var isLoading = false

    fun searchMovies(query: String) {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            _movies.update { Resource.Loading() }
            isLoading = true
            try {
                val response = repository.searchMovies(query)
                _movies.update { Resource.Success(response) }
                //_movies.value = response.results
            } catch (e: Exception) {
                _movies.update { Resource.Error(e.localizedMessage.toString()) }
                // TODO: will be handled later
            } finally {
                isLoading = false
            }
        }
    }
}
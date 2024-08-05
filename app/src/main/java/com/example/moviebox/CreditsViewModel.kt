package com.example.moviebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.model.Actors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditsViewModel
    @Inject
    constructor(
        private val movieCreditsUseCase: GetMovieCreditsUseCase,
    ) : ViewModel() {
        private val _actors = MutableStateFlow<Resource<Actors>>(Resource.Loading())
        val actors: StateFlow<Resource<Actors>> get() = _actors


    private var isLoading = false

    fun getActors(movieId: Int) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            _actors.update { Resource.Loading() }
            isLoading = true

            try {
                val result = movieCreditsUseCase(movieId = movieId)
                _actors.update { Resource.Success(result) }
            } catch (e: Exception) {
                _actors.update { Resource.Error(e.localizedMessage.toString()) }
            } finally {
                isLoading = false
            }
        }
        }
    }

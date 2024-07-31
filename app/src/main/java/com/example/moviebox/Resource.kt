package com.example.moviebox

sealed class Resource<out T> {
    data class Loading<out T>(
        val data: T? = null,
    ) : Resource<T>()

    data class Success<out T>(
        val data: T? = null,
    ) : Resource<T>()

    data class Error<out T>(
        val message: String,
        val data: T? = null,
    ) : Resource<T>()
}

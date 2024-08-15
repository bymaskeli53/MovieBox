package com.example.moviebox.util

sealed class Resource<out T> {
    // Use this when some process is ongoing, with optional data to show progress
    data object Loading : Resource<Nothing>()

    // Use this when the data has been successfully retrieved or processed
    data class Success<out T>(
        val data: T,
    ) : Resource<T>()

    // Use this when an error has occurred, with optional data and an exception for more detail
    data class Error<out T>(
        val exception: Exception,
        val data: T? = null,
    ) : Resource<T>()

    // Use this when the system is in an idle state, not doing anything
    data object Idle : Resource<Nothing>()
}

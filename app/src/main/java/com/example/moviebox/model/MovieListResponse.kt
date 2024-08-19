package com.example.moviebox.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    val page: Int,
    @SerializedName("results")
    val movieResponse: List<MovieItem>,
    val total_pages: Int,
    val total_results: Int,
)

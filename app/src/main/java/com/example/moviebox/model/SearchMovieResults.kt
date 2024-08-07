package com.example.moviebox.model

data class SearchMovieResults(
    val page: Int,
    val results: List<SearchResult>,
    val total_pages: Int,
    val total_results: Int
)
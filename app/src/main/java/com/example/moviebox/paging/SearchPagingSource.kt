package com.example.moviebox.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebox.model.MovieItem
import com.example.moviebox.remote.MovieApi

class SearchPagingSource(
    private val apiService: MovieApi,
    private val query: String,
) : PagingSource<Int, com.example.moviebox.model.MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val position = params.key ?: 1
        return try {
            val response = apiService.searchMovies2(query = query, page = position)

            val movie = response.movieResponse
            LoadResult.Page(
                data = movie,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movie.isEmpty()) null else position + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.moviebox.model.MovieItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}

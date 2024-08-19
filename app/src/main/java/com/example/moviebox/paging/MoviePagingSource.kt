package com.example.moviebox.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebox.model.MovieItem
import com.example.moviebox.remote.MovieApi

class MoviePagingSource(
    private val movieApi: MovieApi,
) : PagingSource<Int, MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> =
        try {
            val position = params.key ?: 1
            val response = movieApi.getPopularMovies(page = position)
            val movies = response.movieResponse

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isNotEmpty()) position + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}

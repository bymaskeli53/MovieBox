package com.example.moviebox.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebox.model.Result
import com.example.moviebox.remote.MovieApi

class SearchPagingSource(
    private val apiService: MovieApi,
    private val query: String
) : PagingSource<Int, com.example.moviebox.model.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response = apiService.searchMovies2(query, page = position)
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.moviebox.model.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

package com.example.moviebox.remote

import com.example.moviebox.BuildConfig
import com.example.moviebox.model.Movie
import com.example.moviebox.util.NetworkConstants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Header("Authorization") authorizationHeader: String = NetworkConstants.AUTHORIZATION_HEADER,
        @Header("Accept") acceptHeader: String = NetworkConstants.ACCEPT_HEADER,
    ): Movie
}

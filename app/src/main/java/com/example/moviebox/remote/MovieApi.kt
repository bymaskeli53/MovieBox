package com.example.moviebox.remote

import com.example.moviebox.BuildConfig
import com.example.moviebox.model.Actors
import com.example.moviebox.model.Movie
import com.example.moviebox.model.TrailerResponse
import com.example.moviebox.util.NetworkConstants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Header("Authorization") authorizationHeader: String = NetworkConstants.AUTHORIZATION_HEADER,
        @Header("Accept") acceptHeader: String = NetworkConstants.ACCEPT_HEADER,
    ): Movie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ): Actors

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailers(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ): TrailerResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ): Movie
}

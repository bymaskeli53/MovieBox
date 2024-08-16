package com.example.moviebox.di

import android.content.Context
import com.example.moviebox.BuildConfig
import com.example.moviebox.database.MovieDao
import com.example.moviebox.domain.GetPopularMoviesUseCase
import com.example.moviebox.remote.MovieApi
import com.example.moviebox.repository.MovieRepository
import com.example.moviebox.repository.MovieRepositoryImpl
import com.example.moviebox.util.NetworkConnectionInterceptor
import com.example.moviebox.util.constant.DurationConstants.TIMEOUT_DURATION
import com.example.moviebox.util.constant.NetworkConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        return logging
    }

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context,
    ): NetworkConnectionInterceptor = NetworkConnectionInterceptor(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .callTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkConnectionInterceptor)
            .build()

    /**
     * Provide product api
     *
     * @param okHttpClient provided because of having singleton client
     * @see https://github.com/square/okhttp/blob/master/okhttp/src/main/kotlin/okhttp3/OkHttpClient.kt
     *
     *
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApi: MovieApi,
        movieDao: MovieDao,
    ): MovieRepository = MovieRepositoryImpl(movieApi, movieDao)

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository)
}

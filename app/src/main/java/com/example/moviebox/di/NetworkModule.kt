package com.example.moviebox.di

import com.example.moviebox.BuildConfig
import com.example.moviebox.remote.MovieApi
import com.example.moviebox.util.NetworkConstants.BASE_URL
import com.example.moviebox.util.NetworkConstants.TIMEOUT_DURATION
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .callTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
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
    fun provideProductApi(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)
}

package com.example.moviebox.model.mapper

import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.MovieItem

object MovieToMovieEntityMapper : Mapper<MovieItem, MovieEntity> {
    override fun map(from: MovieItem): MovieEntity =
        MovieEntity(
            id = from.id,
            title = from.title ?: "",
            overview = from.overview ?: "",
            releaseDate = from.release_date ?: "",
            isFavorite = from.isFavorite ?: false,
            posterPath = from.poster_path ?: "",
            backdropPath = from.backdrop_path ?: "",
            voteAverage = from.vote_average ?: 0.0,
            voteCount = from.vote_count ?: 0,
            popularity = from.popularity ?: 0.0,
            adult = from.adult ?: false,
            video = from.video ?: false,
            originalLanguage = from.original_language ?: "",
            originalTitle = from.original_title ?: "",

        )
}

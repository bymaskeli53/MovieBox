package com.example.moviebox.model.mapper

import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.MovieItem

object MovieEntityToMovieItemMapper : Mapper<MovieEntity, MovieItem> {

    override fun map(from: MovieEntity): MovieItem {
        return MovieItem(
            id = from.id!!,
            title = from.title,
            overview = from.overview,
            release_date = from.releaseDate,
            isFavorite = from.isFavorite,
            poster_path = from.posterPath,
            backdrop_path = from.backdropPath,
            vote_average = from.voteAverage,
            vote_count = from.voteCount,
            popularity = from.popularity,
            adult = from.adult,
            video = from.video,
            original_language = from.originalLanguage,
            original_title = from.originalTitle,
            genre_ids = emptyList()
            )
        // TODO: Consider about database migration
    }
}
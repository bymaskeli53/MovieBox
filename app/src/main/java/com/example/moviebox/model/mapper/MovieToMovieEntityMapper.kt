package com.example.moviebox.model.mapper

import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.MovieItem

object MovieToMovieEntityMapper : Mapper<MovieItem, MovieEntity> {
    override fun map(from: MovieItem): MovieEntity = MovieEntity(
        id = from.id,
        title = from.title ?: "",
        overview = from.overview ?: "",
        releaseDate = from.release_date ?: "",
        )

}
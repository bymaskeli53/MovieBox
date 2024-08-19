package com.example.moviebox.model.mapper

import com.example.moviebox.database.MovieEntity
import com.example.moviebox.model.Movie
import com.example.moviebox.model.Result

object MovieToMovieEntityMapper : Mapper<Result, MovieEntity> {
    override fun map(from: Result): MovieEntity = MovieEntity(
        id = from.id,
        title = from.title ?: "",
        overview = from.overview ?: "",
        releaseDate = from.release_date ?: "",
        )

}
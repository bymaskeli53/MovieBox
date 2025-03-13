package com.example.moviebox.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviebox.util.constant.DatabaseConstants.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val overview: String,
    val posterPath: String? = null,
    val releaseDate: String,
    var isFavorite: Boolean = false,
    val backdropPath: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val popularity: Double? = null,
    val adult: Boolean? = null,
    val video: Boolean? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    )

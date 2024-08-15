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
)

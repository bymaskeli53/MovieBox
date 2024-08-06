package com.example.moviebox

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviebox.util.DatabaseConstants.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val overview: String,
    val posterPath: String? = null,
    val releaseDate: String,
)

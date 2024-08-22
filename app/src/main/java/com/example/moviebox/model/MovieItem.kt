package com.example.moviebox.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val adult: Boolean? = null,
    val backdrop_path: String? = null,
    val genre_ids: List<Int>? = null,
    val id: Int,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    var release_date: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    var isFavorite: Boolean,
) : Parcelable {
    /**
     * To string method is overriden to show movie title on detail screen
     **/

    override fun toString(): String = this.title ?: "movie name not found"
}

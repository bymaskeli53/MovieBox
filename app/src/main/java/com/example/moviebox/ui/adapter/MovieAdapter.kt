package com.example.moviebox.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.R
import com.example.moviebox.databinding.ItemGridMovieBinding
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.model.MovieItem
import com.example.moviebox.util.constant.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.extension.formatRuntime
import com.example.moviebox.util.extension.hide
import com.example.moviebox.util.extension.show
import java.util.Locale

class MovieAdapter(
    private val isGridLayout: Boolean,
    private val onMovieClick: (MovieItem) -> Unit = {},
) : PagingDataAdapter<MovieItem, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    inner class MovieViewHolder(
        private val binding: ItemMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieItem) {
            binding.movieTitleTextView.text = movie.title
            binding.movieImageView.load(IMAGE_BASE_URL + movie.poster_path) {
                crossfade(CROSSFADE_DURATION)
                placeholder(R.drawable.ic_generic_movie_poster)
            }
            binding.tvReleaseDate.text = movie.release_date?.substringBefore("-")
            binding.tvPopularity.text =
                String.format(locale = Locale.getDefault(), format = "%.1f", movie.vote_average)
            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
            if (movie.isFavorite) {
                binding.ivFavorite.show()
            } else {
                binding.ivFavorite.hide()
            }
            setBackgroundColorByPopularity(movie.vote_average!!, binding.root.context, binding)
        }
    }

    inner class MovieGridViewHolder(
        private val binding: ItemGridMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieItem) {
            binding.ivMovie.load(IMAGE_BASE_URL + movie.poster_path) {
                crossfade(CROSSFADE_DURATION)
                placeholder(R.drawable.ic_generic_movie_poster)
            }
            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        if (isGridLayout) {
            val binding =
                ItemGridMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieGridViewHolder(binding)
        } else {
            val binding =
                ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(binding)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val movie = getItem(position)
        if (movie != null) {
            if (isGridLayout) {
                (holder as MovieGridViewHolder).bind(movie)
            } else {
                (holder as MovieViewHolder).bind(movie)
            }
        }
    }

    private fun setBackgroundColorByPopularity(voteAverage: Double, context: Context, binding: ItemMovieBinding) {

        val popularityRating = PopularityRating.fromVoteAverage(voteAverage)


        val backgroundColor = ContextCompat.getColor(context, popularityRating.colorRes)

        val drawable = binding.ivStar.background as? GradientDrawable
        drawable?.setColor(backgroundColor)

        if (drawable == null) {
            val newDrawable = GradientDrawable()
            newDrawable.shape = GradientDrawable.OVAL
            newDrawable.setColor(backgroundColor)
            binding.ivStar.background = newDrawable
        }
    }


}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(
        oldItem: MovieItem,
        newItem: MovieItem,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MovieItem,
        newItem: MovieItem,
    ) = oldItem == newItem && oldItem.isFavorite == newItem.isFavorite
}

enum class PopularityRating(val colorRes: Int) {
    HIGH(R.color.popularity_high),
    MEDIUM(R.color.popularity_medium),
    LOW(R.color.popularity_low);

    companion object {
        fun fromVoteAverage(voteAverage: Double): PopularityRating {
            return when {
                voteAverage > 8.0 -> HIGH
                voteAverage >= 6.0 -> MEDIUM
                else -> LOW
            }
        }
    }
}

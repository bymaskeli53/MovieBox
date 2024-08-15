package com.example.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.R
import com.example.moviebox.databinding.ItemGridMovieBinding
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.constant.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.extension.hide
import com.example.moviebox.util.extension.show
import java.util.Locale

class MovieAdapter(
    private val isGridLayout: Boolean,
    private val onMovieClick: (Result) -> Unit = {},
) : PagingDataAdapter<Result, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    inner class MovieViewHolder(
        private val binding: ItemMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
            binding.movieTitleTextView.text = movie.title
            binding.movieImageView.load(IMAGE_BASE_URL + movie.poster_path) {
                crossfade(CROSSFADE_DURATION)
                placeholder(R.drawable.ic_generic_movie_poster)

                // transformations(CircleCropTransformation())
            }
            binding.tvReleaseDate.text = movie.release_date.substringBefore("-")
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
            // binding.ivFavorite.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE
        }
    }

    inner class MovieGridViewHolder(
        private val binding: ItemGridMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
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


}

class MovieDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(
        oldItem: Result,
        newItem: Result,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Result,
        newItem: Result,
    ) = oldItem == newItem && oldItem.isFavorite == newItem.isFavorite
}

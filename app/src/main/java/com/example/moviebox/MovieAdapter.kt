package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.databinding.ItemGridMovieBinding
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.NetworkConstants.IMAGE_BASE_URL
import com.example.moviebox.util.hide
import com.example.moviebox.util.show

class MovieAdapter(
    private val isGridLayout: Boolean,
    private val onMovieClick: (Result) -> Unit = {},
) : ListAdapter<Result, RecyclerView.ViewHolder>(MovieDiffCallback()) {
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
            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
            if (movie.isFavorite) {
                binding.ivFavorite.show()
            } else {
                binding.ivFavorite.hide()
            }
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
        if (isGridLayout) {
            (holder as MovieGridViewHolder).bind(movie)
        } else {
            (holder as MovieViewHolder).bind(movie)
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

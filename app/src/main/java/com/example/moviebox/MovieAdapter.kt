package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviebox.databinding.ItemMovieBinding
import com.example.moviebox.model.Result
import com.example.moviebox.util.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.NetworkConstants.IMAGE_BASE_URL

class MovieAdapter : ListAdapter<Result, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {
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
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        holder.bind(movie = getItem(position))
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
    ) = oldItem == newItem
}
